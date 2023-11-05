package com.mondecitronne.homunculus;

import java.io.File;
import java.net.Proxy;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import javax.annotation.Nullable;
import com.google.common.collect.Maps;
import com.mojang.authlib.Agent;
import com.mojang.authlib.GameProfile;
import com.mojang.authlib.GameProfileRepository;
import com.mojang.authlib.ProfileLookupCallback;
import com.mojang.authlib.minecraft.MinecraftSessionService;
import com.mojang.authlib.yggdrasil.YggdrasilAuthenticationService;

public class GameProfileFetcher {
	private static MinecraftSessionService sessionService;
	private static GameProfileRepository profileRepo;
	// there could be contention if thread pool has more than one thread
	private static final ExecutorService THREAD_POOL = new ThreadPoolExecutor(0, 1, 1L, TimeUnit.MINUTES, new LinkedBlockingQueue<Runnable>());
	
	private static final Map<String, GameProfileFetcher> fetchersByName = Maps.<String, GameProfileFetcher>newHashMap();
	private static final Map<UUID, GameProfileFetcher> fetchersById = Maps.<UUID, GameProfileFetcher>newHashMap();
	
	@Nullable
	private GameProfile profile;
    
    public static void init(File dataDir) {
    	YggdrasilAuthenticationService auth = new YggdrasilAuthenticationService(Proxy.NO_PROXY, UUID.randomUUID().toString());
    	sessionService = auth.createMinecraftSessionService();
    	profileRepo = auth.createProfileRepository();
    }
	
	private GameProfileFetcher(GameProfile profileIn) {
		dispatchFetchProfile(profileIn);
	}
	
	public static GameProfileFetcher fetchProfile(GameProfile profile) {
		if (profile.getId() != null && fetchersById.containsKey(profile.getId())) {
			// if the profile specifies an ID, ensure that the fetcher with the correct ID returns, as it will reject a profile with the wrong ID
			return fetchersById.get(profile.getId());
		} else if (fetchersByName.containsKey(profile.getName())) {
			return fetchersByName.get(profile.getName());
		} else {
			GameProfileFetcher fetcher = new GameProfileFetcher(profile);
			fetchersByName.put(profile.getName(), fetcher);
			if (profile.getId() != null) {
				fetchersById.put(profile.getId(), fetcher);
			}
			return fetcher;
		}
	}
	
	@Nullable
	public synchronized GameProfile getGameProfile() {
 		return profile;
	}
	
    private void dispatchFetchProfile(GameProfile profileIn) { 
    	THREAD_POOL.submit(new Runnable() {
            public void run() {
            	class LocalCallback implements ProfileLookupCallback {
            		public GameProfile fetched;
                    public void onProfileLookupSucceeded(GameProfile success) {
                        fetched = success;
                    }
                    public void onProfileLookupFailed(GameProfile failed, Exception exception) {
                        fetched = null;
                    }
                };
            	LocalCallback callback = new LocalCallback(); 
            	profileRepo.findProfilesByNames(new String[] {profileIn.getName()}, Agent.MINECRAFT, callback);
            	
            	GameProfile fetched = sessionService.fillProfileProperties(callback.fetched, false);
            	if (profileIn.getId() == null || fetched.getId().equals(profileIn.getId())) {
            		synchronized (this) {
            			profile = fetched;
            		}
            	}
            }
        });
    }
}
