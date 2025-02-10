package org.example.demo;

import javafx.fxml.FXML;
import javafx.scene.chart.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.Alert;
import javafx.scene.layout.VBox;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.*;

public class PlaylistController {

    @FXML
    private Button homeButton;

    @FXML
    private VBox mainContent;

    @FXML
    private ListView<String> playlistView;

    @FXML
    private Label welcomeText;

    @FXML
    private BarChart<String, Number> barChart;

    @FXML
    private CategoryAxis xAxis;

    @FXML
    private NumberAxis yAxis;

    @FXML
    private PieChart pieChart;

    private LinkedList<Song> iconicTopBillboard = new LinkedList<>();
    private LinkedList<Song> iconicTopLastfm = new LinkedList<>();
    private LinkedList<Song> iconicBBCPlaylist = new LinkedList<>();
    private LinkedList<Song> iconicPlaylist = new LinkedList<>();
    private LinkedList<Song> suggestedPlaylist = new LinkedList<>();

    /** Handles "Get Started" button click */
    @FXML
    private void onHelloButtonClick() {
        mainContent.setVisible(true);
        homeButton.setVisible(false);
        welcomeText.setText("Music Top Charts");
    }

    /** Fetch Billboard Songs */
    public void fetchBillboardSongs() {
        iconicTopBillboard.clear();
        iconicTopBillboard.addAll(scrapeSongs(
                "https://www.billboard.com/charts/hot-100/",
                "h3#title-of-a-story.c-title",
                "span.c-label.a-no-trucate",
                "Pop/Various"
        ));
        updateListView(iconicTopBillboard);
        showSuccess("Billboard songs loaded successfully!");
        updateBarChart();
    }

    /** Fetch Last.fm Songs */
    public void fetchLastfmSongs() {
        iconicTopLastfm.clear();
        iconicTopLastfm.addAll(scrapeSongs(
                "https://www.last.fm/charts",
                "td a.link-block-target",
                "td.globalchart-track-artist-name a",
                "Various"
        ));
        updateListView(iconicTopLastfm);
        showSuccess("Last.fm songs loaded successfully!");
        updateBarChart();
    }

    /** Fetch BBC Songs */
    public void fetchBBCSongs() {
        iconicBBCPlaylist.clear();
        iconicBBCPlaylist.addAll(scrapeSongs(
                "https://www.officialcharts.com/charts/singles-chart/",
                "a.chart-name span",
                "a.chart-artist span",
                "Various"
        ));
        updateListView(iconicBBCPlaylist);
        showSuccess("BBC songs loaded successfully!");
        updateBarChart();
    }

    /** Generate Suggested Playlist Based on Popularity */
    public void generateSuggestedPlaylist() {
        // Ensure data is loaded before generating playlist
        if (iconicTopBillboard.isEmpty()) fetchBillboardSongs();
        if (iconicTopLastfm.isEmpty()) fetchLastfmSongs();
        if (iconicBBCPlaylist.isEmpty()) fetchBBCSongs();

        // Combine all fetched songs
        iconicPlaylist.clear();
        iconicPlaylist.addAll(iconicTopBillboard);
        iconicPlaylist.addAll(iconicTopLastfm);
        iconicPlaylist.addAll(iconicBBCPlaylist);

        // Count occurrences of each song
        Map<String, Integer> songCount = new HashMap<>();
        LinkedList<Song> temp = new LinkedList<>();
        HashSet<String> addedSongs = new HashSet<>();

        for (Song song : iconicPlaylist) {
            String title = song.getTitle().toLowerCase();
            songCount.put(title, songCount.getOrDefault(title, 0) + 1);

            // If song appears in at least two sources, add to suggested playlist
            if (songCount.get(title) >= 2 && addedSongs.add(title)) {
                temp.add(song);
            }
        }

        // Shuffle and limit to top 10 songs
        suggestedPlaylist.clear();
        if (temp.size() > 10) {
            Collections.shuffle(temp);
            for (int i = 0; i < 10; i++) {
                suggestedPlaylist.add(temp.get(i));
            }
        } else {
            suggestedPlaylist.addAll(temp);
        }

        updateListView(suggestedPlaylist);
        updateBarChart();
        barChart.setVisible(true);
        showSuccess("Suggested playlist generated!");
    }

    /** Reusable Web Scraping Method */
    private LinkedList<Song> scrapeSongs(String url, String titleSelector, String artistSelector, String defaultGenre) {
        LinkedList<Song> playlist = new LinkedList<>();
        try {
            Document doc = Jsoup.connect(url)
                    .userAgent("Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/605.1.15 (KHTML, like Gecko) Safari/605.1.15")
                    .header("Accept-Language", "*")
                    .get();

            Elements songTitles = doc.select(titleSelector);
            Elements artists = doc.select(artistSelector);

            for (int i = 0; i < songTitles.size(); i++) {
                String title = songTitles.get(i).text().trim();
                String artist = (i < artists.size()) ? artists.get(i).text().trim() : "Unknown Artist";
                playlist.add(new Song(title, artist, defaultGenre));
            }
        } catch (IOException e) {
            showError("Error fetching songs from " + url);
        }
        return playlist;
    }

    /** Update ListView UI */
    private void updateListView(LinkedList<Song> playlist) {
        playlistView.getItems().clear();
        for (Song song : playlist) {
            playlistView.getItems().add(song.getTitle() + " - " + song.getArtist() + " (" + song.getGenre() + ")");
        }
    }

    /**
     * Update BarChart with Song Occurrences
     */
    private void updateBarChart() {
        // Clear previous data in the BarChart
        barChart.getData().clear();

        // Initialize map to count songs for each artist
        Map<String, Integer> artistSongCount = new HashMap<>();
        int totalSongs = 0;

        // Count songs per artist and calculate total songs
        for (Song song : iconicTopBillboard) {
            if (!song.getArtist().equalsIgnoreCase("Unknown Artist")) {
                artistSongCount.put(song.getArtist(), artistSongCount.getOrDefault(song.getArtist(), 0) + 1);
                totalSongs++;
            }
        }

        for (Song song : iconicTopLastfm) {
            if (!song.getArtist().equalsIgnoreCase("Unknown Artist")) {
                artistSongCount.put(song.getArtist(), artistSongCount.getOrDefault(song.getArtist(), 0) + 1);
                totalSongs++;
            }
        }

        for (Song song : iconicBBCPlaylist) {
            if (!song.getArtist().equalsIgnoreCase("Unknown Artist")) {
                artistSongCount.put(song.getArtist(), artistSongCount.getOrDefault(song.getArtist(), 0) + 1);
                totalSongs++;
            }
        }

        // Create a BarChart series to show the percentage of songs per artist
        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.setName("Artist Song Percentage");

        // Add each artist's song count and percentage (multiplied by 100) to the BarChart
        for (Map.Entry<String, Integer> entry : artistSongCount.entrySet()) {
            double percentage = (entry.getValue() / (double) totalSongs) * 100; // Calculate percentage
            series.getData().add(new XYChart.Data<>(entry.getKey(), percentage)); // Add scaled percentage
        }

        // Add the series to the BarChart
        barChart.getData().add(series);
    }



    /** Show Success Message */
    private void showSuccess(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Success");
        alert.setContentText(message);
        alert.showAndWait();
    }

    /** Show Error Message */
    private void showError(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setContentText(message);
        alert.showAndWait();
    }
}
