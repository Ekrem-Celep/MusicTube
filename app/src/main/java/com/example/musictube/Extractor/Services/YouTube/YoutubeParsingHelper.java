package com.example.musictube.Extractor.Services.YouTube;

import com.example.musictube.Extractor.ParsingException;

/**
 * Created by Ekrem on 2016-05-08.
 */
public class YoutubeParsingHelper {

    private YoutubeParsingHelper() {
    }

    public static int parseDurationString(String input)
            throws ParsingException, NumberFormatException {
        String[] splitInput = input.split(":");
        String days = "0";
        String hours = "0";
        String minutes = "0";
        String seconds;

        switch(splitInput.length) {
            case 4:
                days = splitInput[0];
                hours = splitInput[1];
                minutes = splitInput[2];
                seconds = splitInput[3];
                break;
            case 3:
                hours = splitInput[0];
                minutes = splitInput[1];
                seconds = splitInput[2];
                break;
            case 2:
                minutes = splitInput[0];
                seconds = splitInput[1];
                break;
            case 1:
                seconds = splitInput[0];
                break;
            default:
                throw new ParsingException("Error duration string with unknown format: " + input);
        }
        return ((((Integer.parseInt(days) * 24)
                + Integer.parseInt(hours) * 60)
                + Integer.parseInt(minutes)) * 60)
                + Integer.parseInt(seconds);
    }
}
