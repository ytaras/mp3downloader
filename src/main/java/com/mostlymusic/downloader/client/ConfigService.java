package com.mostlymusic.downloader.client;

import java.io.IOException;

import com.mostlymusic.downloader.client.exceptions.RequestException;

public interface ConfigService {
    Config getConfig() throws IOException, RequestException;
}
