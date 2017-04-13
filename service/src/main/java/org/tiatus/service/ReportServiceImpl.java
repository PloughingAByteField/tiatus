package org.tiatus.service;

import be.quodlibet.boxable.BaseTable;
import be.quodlibet.boxable.Cell;
import be.quodlibet.boxable.Row;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.PDPageTree;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tiatus.entity.*;

import javax.inject.Inject;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.sql.Timestamp;
import java.time.Duration;
import java.time.Instant;
import java.util.*;
import java.util.List;

/**
 * Created by johnreynolds on 07/04/2017.
 */
public class ReportServiceImpl implements ReportService {

    private static final Logger LOG = LoggerFactory.getLogger(ReportServiceImpl.class);
    private static final String JBOSS_HOME_DIR = "jboss.home.dir";

    private EntryService entryService;
    private TimesService timesService;
    private ConfigService configService;

    private Locale currentLocale;
    private ResourceBundle messages;

    @Inject
    public ReportServiceImpl(ConfigService configService, EntryService entryService, TimesService timesService) {
        this.configService = configService;
        this.entryService = entryService;
        this.timesService = timesService;
        // TODO pull language and country from config service which is to be configured as part of setup
        String language = "en";
        String country = "GB";
        this.currentLocale = new Locale(language, country);
        this.messages = ResourceBundle.getBundle("org.tiatus.service.ReportService", currentLocale);
    }

    @Override
    public void createReportForRace(Race race) {
        LOG.debug("Got report creation request for race " + race.getName());
        try {
            createPdfReports(race);
        } catch (ServiceException | IOException | URISyntaxException e) {
            LOG.warn("Failed to create report ", e);
        }
    }

    private void createPdfReports(Race race) throws ServiceException, IOException, URISyntaxException {
        Date now = new Date();
        String logoFileName = configService.getEventLogo();
        File logoFile = new File(System.getProperty(JBOSS_HOME_DIR) + logoFileName);
        String title = configService.getEventTitle() + " " + race.getName();
        List<Entry> entries = entryService.getEntriesForRace(race);
        List<EntryPositionTime> times = timesService.getAllTimesForRace(race);
        // create array of entries by starting and end position
        List<EntriesForEventPositions> entriesByEventPositions = getEntriesSortedForEventPositions(entries);

        createByTimePdfReport(title, logoFile, race, now, entriesByEventPositions, times);
        createByEventPdfReport(title, logoFile, race, now, entriesByEventPositions, times);
    }

    private List<EntriesForEventPositions> getEntriesSortedForEventPositions(List<Entry> entries) {
        List<EntriesForEventPositions> list = new ArrayList<>();
        for (Entry entry: entries) {
            if (list.size() == 0) {
                EntriesForEventPositions ep = new EntriesForEventPositions();
                ep.setPositions(entry.getEvent().getPositions());
                ep.addEntry(entry);
                list.add(ep);
                continue;
            }

            EntriesForEventPositions matched = null;
            for (EntriesForEventPositions eventPosition: list) {
                if (doPositionsMatch(eventPosition.getPositions(), entry.getEvent().getPositions())) {
                    matched = eventPosition;
                    break;
                }
            }

            if (matched != null) {
                matched.addEntry(entry);
            } else {
                EntriesForEventPositions ep = new EntriesForEventPositions();
                ep.setPositions(entry.getEvent().getPositions());
                ep.addEntry(entry);
                list.add(ep);
            }
        }
        return list;
    }

    private boolean doPositionsMatch(List<EventPosition> positions, List<EventPosition> positionsForEvent) {
        if (positions.size() == positionsForEvent.size()) {
            for (int i = 0; i < positions.size(); i++) {
                EventPosition position = positions.get(i);
                EventPosition positionForEvent = positionsForEvent.get(i);
                if (position.getPosition().getId() != positionForEvent.getPosition().getId()) {
                    return false;
                }
                if (position.getPositionOrder() != positionForEvent.getPositionOrder()) {
                    return false;
                }
            }

            return true;
        }

        return false;
    }

    private void createByTimePdfReport(String title, File logoFile, Race race, Date now, List<EntriesForEventPositions> entriesByEventPositions, List<EntryPositionTime> times) throws ServiceException, IOException, URISyntaxException {
        String fileName = "/tiatus/results/" + race.getName() + "_ByTime.pdf";
        File resultsFile = new File(System.getProperty(JBOSS_HOME_DIR) + fileName);
        resultsFile.getParentFile().mkdirs();
        // sort by time

        createPdfReport(resultsFile, title, logoFile, "by_time", now, entriesByEventPositions, times);
    }

    private void createByEventPdfReport(String title, File logoFile, Race race, Date now, List<EntriesForEventPositions> entriesByEventPositions, List<EntryPositionTime> times) throws ServiceException, IOException, URISyntaxException {
        String fileName = "/tiatus/results/" + race.getName() + "_ByEvent.pdf";
        File resultsFile = new File(System.getProperty(JBOSS_HOME_DIR) + fileName);
        resultsFile.getParentFile().mkdirs();
        // sort by event
        createPdfReport(resultsFile, title, logoFile, "by_event", now, entriesByEventPositions, times);
    }

    private PDImageXObject getLogoImage(File logoFile, PDDocument document) throws IOException {
        PDImageXObject pdImage = PDImageXObject.createFromFileByExtension(logoFile, document);
        return pdImage;
    }

    private void fillHeader(PDDocument document, PDPage page, PDImageXObject pdImage, String title, float scale, float yStartNewPage, String reportType, Date now) throws IOException {
        PDPageContentStream contentStream = new PDPageContentStream(document, page, PDPageContentStream.AppendMode.OVERWRITE, true);
        contentStream.drawImage(pdImage, 20, yStartNewPage - (pdImage.getHeight() * scale), pdImage.getWidth()*scale, pdImage.getHeight()*scale);

        int fontSize = 12;
        float center = page.getMediaBox().getWidth() / 2;
        float titleWidth = PDType1Font.HELVETICA_BOLD.getStringWidth(title) / 1000 * fontSize;
        contentStream.beginText();
        contentStream.setFont(PDType1Font.HELVETICA_BOLD, fontSize);
        contentStream.newLineAtOffset(center - (titleWidth / 2), yStartNewPage - (pdImage.getHeight() * scale)/2 + 20);
        contentStream.showText(title);
        contentStream.endText();

        String report = messages.getString(reportType);
        float reportWidth = PDType1Font.HELVETICA_BOLD.getStringWidth(report) / 1000 * fontSize;
        contentStream.beginText();
        contentStream.setFont(PDType1Font.HELVETICA_BOLD, fontSize);
        contentStream.newLineAtOffset(center - (reportWidth / 2), yStartNewPage - (pdImage.getHeight() * scale)/2);
        contentStream.showText(report);
        contentStream.endText();

        int correctAsFontSize = 10;
        String correct = messages.getString("correct_as_of") + " " + now;
        float correctWidth = PDType1Font.HELVETICA_BOLD.getStringWidth(correct) / 1000 * correctAsFontSize;
        contentStream.beginText();
        contentStream.setFont(PDType1Font.HELVETICA, correctAsFontSize);
        contentStream.newLineAtOffset(center - (correctWidth / 2), yStartNewPage - (pdImage.getHeight() * scale)/2 - 50);
        contentStream.showText(correct);
        contentStream.endText();

        contentStream.close();
    }

    private void fillFooter(PDDocument document, Date now, float margin) throws IOException {
        PDPageTree pages = document.getDocumentCatalog().getPages();
        int numberOfPages = pages.getCount();
        int count = 1;
        Iterator<PDPage> iterator = pages.iterator();
        PDFont footerFont = PDType1Font.TIMES_ROMAN;
        while (iterator.hasNext()) {
            PDPage pdPage = iterator.next();
            String text = messages.getString("page") + " " + count + " " + messages.getString("of") + " " + numberOfPages;
            PDPageContentStream footer = new PDPageContentStream(document, pdPage, PDPageContentStream.AppendMode.APPEND, true);
            footer.beginText();
            footer.setFont(footerFont, 10);
            footer.newLineAtOffset(pdPage.getMediaBox().getWidth()/2 - (text.length()/2), margin);
            footer.showText(text);
            footer.endText();
            footer.close();

            PDPageContentStream footerDate = new PDPageContentStream(document, pdPage, PDPageContentStream.AppendMode.APPEND, true);
            footerDate.beginText();
            footerDate.setFont(footerFont, 6);
            footerDate.newLineAtOffset(pdPage.getMediaBox().getWidth()/2 + 200, margin);
            footerDate.showText("(" + now + ")");
            footerDate.endText();
            footerDate.close();

            count++;
        }
    }

    private Cell<PDPage> createCellForRow(Row<PDPage> row, int width, String data, boolean fastestInSection) {
        Cell<PDPage> cell = row.createCell(width, data);
        cell.setFont(PDType1Font.HELVETICA);
        cell.setFontSize(10);
        if (fastestInSection) {
            cell.setFillColor(Color.YELLOW);
        }
        return cell;
    }

    private void createPdfReport(File resultsFile, String title, File logoFile, String reportType, Date now, List<EntriesForEventPositions> entriesByEventPositions, List<EntryPositionTime> times) throws ServiceException, IOException, URISyntaxException {

        PDDocument document = new PDDocument();
        PDPage page = new PDPage(new PDRectangle(PDRectangle.A4.getHeight(), PDRectangle.A4.getWidth()));
        document.addPage(page);

        PDImageXObject pdImage = getLogoImage(logoFile, document);

        float margin = 10;
        float scale = 0.15f;
        float yStartNewPage = page.getMediaBox().getHeight() - (2 * margin);

        fillHeader(document, page, pdImage, title, scale, yStartNewPage, reportType, now);

        float tableWidth = page.getMediaBox().getWidth() - (2 * margin);
        boolean drawContent = true;
        float yStart = yStartNewPage - (pdImage.getHeight() * scale) - 10;
        float bottomMargin = 20;

        boolean firstTable = true;
        for (EntriesForEventPositions e: entriesByEventPositions) {
            if (firstTable) {
                firstTable = false;
            } else {
                page = new PDPage(new PDRectangle(PDRectangle.A4.getHeight(), PDRectangle.A4.getWidth()));
                document.addPage(page);
                yStart = yStartNewPage - 10;
            }
            BaseTable table = new BaseTable(yStart, yStartNewPage, bottomMargin, tableWidth, margin, document, page, true, drawContent);

            Row<PDPage> positionsRow = table.createRow(15f);
            String positions = null;
            if (e.getPositions().size() > 0) {
                positions = e.getPositions().get(0).getPosition().getName() + " " + messages.getString("to") + " " + e.getPositions().get(e.getPositions().size() - 1).getPosition().getName();
            }
            Cell<PDPage> cell = positionsRow.createCell(100, positions);
            cell.setFont(PDType1Font.HELVETICA_BOLD);
            cell.setFillColor(Color.BLACK);
            cell.setTextColor(Color.WHITE);
            table.addHeaderRow(positionsRow);

            List<Entry> entries = e.getEntries();
            fillResults(entries, table, times);
            yStart = table.draw();
        }

        fillFooter(document, now, margin);

        document.save(resultsFile);
        document.close();
    }

    private void addHeaderCell(int width, String text, Row<PDPage> headerRow)  {
        Cell<PDPage> cell = headerRow.createCell(width, text);
        cell.setFont(PDType1Font.HELVETICA);
        cell.setFillColor(Color.BLACK);
        cell.setTextColor(Color.WHITE);
    }

    private void fillResults(List<Entry> entries, BaseTable table, List<EntryPositionTime> times) {
        // need to scale based on the number of positions all has to add to 100
        List<EventPosition> positions = entries.get(0).getEvent().getPositions();
        int numberOfPositions = positions.size() - 1;
        float totalColumnWidths = 80f;
        if (numberOfPositions > 0) {
            totalColumnWidths += numberOfPositions * 10;
        }
        float onePercentWidth = 100 / totalColumnWidths;
        int numberWidth = (int)(10 * onePercentWidth);
        int eventWidth = (int)(20 * onePercentWidth);
        int clubWidth = (int)(20 * onePercentWidth);
        int crewWidth = (int)(10 * onePercentWidth);
        int positionWidth = (int)(10 * onePercentWidth);
        int commentWidth = (int)(20 * onePercentWidth);
        int left;
        if (numberOfPositions > 0) {
            left = 100 - (numberWidth + eventWidth + clubWidth + crewWidth + commentWidth + (positionWidth * numberOfPositions));
        } else {
            left = 100 - (numberWidth + eventWidth + clubWidth + crewWidth + commentWidth);
        }
        commentWidth += left;

        Row<PDPage> headerRow = table.createRow(15f);
        addHeaderCell(numberWidth, messages.getString("number"), headerRow);
        addHeaderCell(eventWidth, messages.getString("event"), headerRow);
        addHeaderCell(clubWidth, messages.getString("club"), headerRow);
        addHeaderCell(crewWidth, messages.getString("details"), headerRow);
        if (numberOfPositions > 0) {
            for (int i = 1; i <= numberOfPositions; i++) {
                Position position = positions.get(i).getPosition();
                addHeaderCell(positionWidth, position.getName(), headerRow);
            }
        }
        addHeaderCell(commentWidth, messages.getString("comment"), headerRow);
        table.addHeaderRow(headerRow);

        for (Entry entry: entries) {
            Row<PDPage> row = table.createRow(10f);
            boolean fastestInSection = false;
            Cell<PDPage> number = createCellForRow(row, numberWidth, entry.getNumber().toString(), fastestInSection);
            Cell<PDPage> event = createCellForRow(row, eventWidth, entry.getEvent().getName(), fastestInSection);
            Cell<PDPage> club = createCellForRow(row, clubWidth, getClubsForEntry(entry), fastestInSection);
            Cell<PDPage> crew = createCellForRow(row, crewWidth, entry.getCrew(), fastestInSection);
            if (entry.getEvent().getPositions().size() > 0) {
                List<EntryPositionTime> entryTimes = getPositionTimesForEntryByPosition(entry, times);
                Position startingPosition = entry.getEvent().getPositions().get(0).getPosition();
                Timestamp startTimestamp = getTimeForPosition(startingPosition, entryTimes);
                Instant startingPositionInstant = null;
                if (startTimestamp != null) {
                    startingPositionInstant = startTimestamp.toInstant();
                }
                for (int i = 1; i < entry.getEvent().getPositions().size(); i++) {
                    Position position = entry.getEvent().getPositions().get(i).getPosition();
                    Timestamp positionTimestamp = getTimeForPosition(position, entryTimes);
                    if (positionTimestamp != null && startTimestamp != null) {
                        Instant positionInstant = positionTimestamp.toInstant();
                        Duration timeToPosition = Duration.between(startingPositionInstant, positionInstant);
                        String time = getTimeForDuration(timeToPosition);
                        createCellForRow(row, positionWidth, time, fastestInSection);
                    } else {
                        createCellForRow(row, positionWidth, "", fastestInSection);
                    }
                }
            }
            Cell<PDPage> comment = createCellForRow(row, commentWidth, "", fastestInSection);
        }
    }

    private String getTimeForDuration(Duration duration) {
        String time = "";
        long hours = duration.toHours();
        if (hours < 10) {
            time = time + "0";
        }
        time = time + hours + ":";
        long minutes = duration.minusHours(hours).toMinutes();
        if (minutes < 10) {
            time = time + "0";
        }
        time = time + minutes + ":";
        long seconds = duration.minusHours(hours).minusMinutes(minutes).toMillis()/1000;
        if (seconds < 10) {
            time = time + "0";
        }
        time = time + seconds;

        return time;
    }

    private Timestamp getTimeForPosition(Position position, List<EntryPositionTime> entryTimes) {
        for (EntryPositionTime entryPositionTime: entryTimes) {
            if (entryPositionTime.getPosition().getId() == position.getId()) {
                return entryPositionTime.getTime();
            }
        }
        return null;
    }

    private List<EntryPositionTime> getPositionTimesForEntryByPosition(Entry entry, List<EntryPositionTime> times) {
        List<EntryPositionTime> entryPositionTimes = new ArrayList<>();
        for (EntryPositionTime time: times) {
            if (time.getEntry().getId() == entry.getId()) {
                entryPositionTimes.add(time);
            }
        }
        List<EventPosition> eventPositions = entry.getEvent().getPositions();

        entryPositionTimes.sort(new Comparator<EntryPositionTime>() {
            @Override
            public int compare(EntryPositionTime t1, EntryPositionTime t2) {
                Integer t1PositionOrder = 0;
                Integer t2PositionOrder = 0;
                for (EventPosition eventPosition : eventPositions) {
                    if (eventPosition.getPosition().getId() == t1.getPosition().getId()) {
                        t1PositionOrder = eventPosition.getPositionOrder();
                    }
                    if (eventPosition.getPosition().getId() == t2.getPosition().getId()) {
                        t2PositionOrder = eventPosition.getPositionOrder();
                    }
                }

                return t2PositionOrder - t1PositionOrder;
            }
        });

        return entryPositionTimes;
    }

    private String getClubsForEntry(Entry entry) {
        StringBuffer buffer = new StringBuffer();
        for (Club club: entry.getClubs()) {
            if (buffer.length() > 0) {
                buffer.append("/");
            }
            buffer.append(club.getClubName());
        }
        return buffer.toString();
    }
}
