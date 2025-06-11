package com.shopu.common.utils;

import java.net.URI;
public class VideoUtils {

    // Extract Video ID from URL
    public static String extractVideoId(String videoUrl) {
        try {
            URI uri = new URI(videoUrl);
            String host = uri.getHost();
            if (host == null) return null;

            // Handle youtu.be/<videoId>
            if (host.contains("youtu.be")) {
                return uri.getPath().substring(1);
            }

            // Handle youtube.com/watch?v=<videoId>
            if (host.contains("youtube.com")) {
                String query = uri.getQuery();
                if (query != null) {
                    for (String param : query.split("&")) {
                        if (param.startsWith("v=")) {
                            return param.split("=")[1];
                        }
                    }
                }
            }
        } catch (Exception e) {
            return null;
        }
        return null;
    }


    // Generate Thumbnail URL
    public static String generateThumbnailUrl(String videoUrl) {
        String videoId = extractVideoId(videoUrl);
        return videoId != null ? "https://img.youtube.com/vi/" + videoId + "/maxresdefault.jpg" : null;
    }
}