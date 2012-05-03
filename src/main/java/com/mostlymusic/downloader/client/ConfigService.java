package com.mostlymusic.downloader.client;

import com.mostlymusic.downloader.client.exceptions.RequestException;

import java.io.IOException;

public interface ConfigService {
    Config getConfig() throws IOException, RequestException;
}
