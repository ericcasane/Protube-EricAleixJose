package com.tecnocampus.LS2.protube_back;

import com.tecnocampus.LS2.protube_back.application.service.VideoLoaderService;
import com.tecnocampus.LS2.protube_back.domain.model.Video;
import com.tecnocampus.LS2.protube_back.domain.service.VideoService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.ApplicationArguments;
import org.springframework.core.env.Environment;

import java.nio.file.Path;
import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AppStartupRunnerTest {

    @Mock
    private VideoService videoService;

    @Mock
    private VideoLoaderService videoLoaderService;

    @Mock
    private Environment env;

    @Mock
    private ApplicationArguments args;

    private AppStartupRunner appStartupRunner;

    @Test
    void run_shouldLoadInitialData_whenPropertyIsTrue() throws Exception {
        // Arrange
        // We use a path that likely doesn't exist or is just a string to pass the constructor logic
        when(env.getProperty("pro_tube.store.dir")).thenReturn("dummy-path");
        when(env.getProperty("pro_tube.load_initial_data", Boolean.class)).thenReturn(true);
        
        List<Video> mockVideos = Collections.singletonList(new Video());
        when(videoLoaderService.loadVideos(any(Path.class))).thenReturn(mockVideos);

        appStartupRunner = new AppStartupRunner(env);
        // Inject dependencies manually since we are not using Spring context in this unit test
        appStartupRunner.videoService = videoService;
        appStartupRunner.videoLoaderService = videoLoaderService;

        // Act
        appStartupRunner.run(args);

        // Assert
        verify(videoLoaderService).loadVideos(any(Path.class));
        verify(videoLoaderService).displayVideos(mockVideos);
        verify(videoService).saveVideo(any(Video.class));
    }

    @Test
    void run_shouldNotLoadInitialData_whenPropertyIsFalse() throws Exception {
        // Arrange
        when(env.getProperty("pro_tube.store.dir")).thenReturn("dummy-path");
        when(env.getProperty("pro_tube.load_initial_data", Boolean.class)).thenReturn(false);

        appStartupRunner = new AppStartupRunner(env);
        appStartupRunner.videoService = videoService;
        appStartupRunner.videoLoaderService = videoLoaderService;

        // Act
        appStartupRunner.run(args);

        // Assert
        verify(videoLoaderService, never()).loadVideos(any(Path.class));
        verify(videoService, never()).saveVideo(any(Video.class));
    }
}
