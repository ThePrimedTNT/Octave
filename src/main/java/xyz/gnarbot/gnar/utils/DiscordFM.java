package xyz.gnarbot.gnar.utils;

import org.apache.commons.io.IOUtils;
import xyz.gnarbot.gnar.Bot;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class DiscordFM {
    public static final String[] LIBRARIES = {
            "electro-hub", "chill-corner", "korean-madness",
            "japanese-lounge", "classical", "retro-renegade",
            "metal-mix", "hip-hop", "electro-swing",
            "purely-pop", "rock-n-roll", "coffee-house-jazz"
    };

    private static Map<String, List<String>> cache = new HashMap<>(LIBRARIES.length);

    public DiscordFM() {
        for (String lib : LIBRARIES) {
            try (InputStream is = DiscordFM.class.getResourceAsStream("/dfm/" + lib + ".json")) {
                for (String s : IOUtils.toString(is, StandardCharsets.UTF_8).trim().split("\n")) {
                    List<String> collect = Arrays.stream(s.split("\n"))
                            .parallel()
                            .filter(si -> si.startsWith("https://"))
                            .collect(Collectors.toList());
                    cache.put(lib, collect);

                    Bot.getLogger().info("(DiscordFM) Added " + collect.size() + " elements to playlist: " + lib);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public String getRandomSong(String library) {
        try {
            List<String> urls = cache.get(library);
            return urls.get((int) (Math.random() * urls.size()));
        } catch (Exception e) {
            Bot.getLogger().error("DiscordFM Error", e);
            return "https://www.youtube.com/watch?v=D7npse9n-Yw"; //Technical Difficulties video
        }
    }
}
