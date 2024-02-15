package org.tiatus.service;

import org.apache.commons.io.ByteOrderMark;
import org.apache.commons.io.input.BOMInputStream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.tiatus.entity.Club;
import org.tiatus.entity.Entry;
import org.tiatus.entity.Event;
import org.tiatus.entity.Race;
import org.tiatus.entity.RaceEvent;

import com.opencsv.bean.CsvToBeanBuilder;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by johnreynolds on 21/12/2015.
 */
@Service
public class CSVEntriesLoader implements EntryProcessor {

    private static final Logger LOG = LoggerFactory.getLogger(CSVEntriesLoader.class);

    @Autowired
    private EntryService entriesService;

    @Autowired
    private RaceService racesService;

    @Autowired
    private ClubService clubsService;

    @Autowired
    protected EventService eventService;

    @Autowired
    protected EntryService entryService;

    // have races created with template positions, use 1 entry for template position
    // walk list find start time and group by it, match times to race order
    // for each race grouping, create event if it does not exist
    // do entry -- if event does not exist create it
    /*
     Event Name,Race,Number,Club,Name,Weighting,Time Only
     */
    @Override
    public FileProcessingResult processEntriesFile(InputStream contents, String fileName) throws ServiceException {

// build up map of races, events, entries
        List<Race> races = racesService.getRaces();
        List<Event> events = eventService.getEvents();
        List<Club> clubs = clubsService.getClubs();
        
        List<Entry> newEntries = new ArrayList<>();
        LOG.debug("Parsing contents of " + fileName);
        try {
            FileProcessingResult result = new FileProcessingResult();
            List<EntryLine> entryList = getEntriesForList(contents);
            HashMap<Race, Map<Event, List<Entry>>> eventEntries = new HashMap<Race, Map<Event, List<Entry>>>();
            
            Map<Race, Integer> raceNumbers = new HashMap<>();
            // races and events should be preloaded, error out if we get one that does not match
            for (EntryLine line: entryList) {
                LOG.debug("Have entry " + line.getNumber() + " for event " + line.getEvent() + " race " + line.getRace() + " club " + line.getClub() + " to " + line.getTimeOnly());
                
                Race race = getRaceForName(races, line.getRace());
                if (race == null) {
                    LOG.warn("Got invalid race " + line.getRace());
                    result.setCode(HttpStatus.NOT_ACCEPTABLE.value());
                    return result;
                }

                Map<Event, List<Entry>> eventEntriesForRace;
                if(!eventEntries.containsKey(race)) {
                    eventEntriesForRace = new HashMap<Event, List<Entry>>();
                    eventEntries.put(race, eventEntriesForRace);
                } 
                    
                eventEntriesForRace = eventEntries.get(race);

                Event event = getEventForName(events, line.getEvent());
                if (event == null) {
                    LOG.warn("Got invalid event " + line.getEvent());
                    result.setCode(HttpStatus.NOT_ACCEPTABLE.value());
                    return result;
                }
            }
            
            // all good we can wipe existing entries and create new ones
            for (Race race : races) {
                LOG.debug("Wiping existing entries for race " + race.getName() + " race id " + race.getId());
                entriesService.deleteEntriesForRace(race);
                raceNumbers.put(race, Integer.valueOf(0));
            }

            for (EntryLine line: entryList) {
                LOG.debug(line.toString());
                Race race = getRaceForName(races, line.getRace());

                // create Entry
                Entry entry = new Entry();
                Event event = getEventForName(events, line.getEvent());
                entry.setEvent(event);
                entry.setRace(race);
                if (line.getNumber() != null && line.getNumber() > 0) { 
                    entry.setNumber(line.getNumber());
                } else {
                    // use race
                    Integer value = raceNumbers.get(race) + 1;
                    entry.setNumber(value);
                    raceNumbers.put(race, value);
                }

                entry.setTimeOnly(line.getTimeOnly());
                entry.setName(line.getName());
                entry.setWeighting(line.getWeighting());
                
                Set<Club> entryClubs = new HashSet<>();
                // check for composite
                String[] compositeClubs = line.getClub().split(" / ");
                for (String s : compositeClubs) {
                    s = s.trim();
                    Club club = getClubForName(clubs, s);
                    if (club == null) {
                        // add club
                        club = new Club();
                        club.setClubName(s);
                        club = clubsService.addClub(club, s);
                        clubs.add(club);
                    }

                    if (!entryClubs.contains(club)) {
                        entryClubs.add(club);
                    }
                }
                
                entry.setClubs(entryClubs);

                entry = entriesService.addEntry(entry, null);
                Map<Event, List<Entry>> eventEntriesForRace = eventEntries.get(race);
                if(!eventEntriesForRace.containsKey(event)) {
                    eventEntriesForRace = new HashMap<Event, List<Entry>>();
                    eventEntriesForRace.put(event, new ArrayList<>());
                } 

                List<Entry> eventEntriesList = eventEntriesForRace.get(event);
                eventEntriesList.add(entry);
                newEntries.add(entry);
            }

            LOG.debug("Completed load ");
            result.setCode(200);

            return result;

        } catch(ServiceException e) {
            LOG.warn("failed", e);
            FileProcessingResult result = new FileProcessingResult();
            result.setData("File processing error");
            result.setCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
            return result;

        } catch(Exception e) {
            LOG.warn("failed", e);
            FileProcessingResult result = new FileProcessingResult();
            result.setData(e.getLocalizedMessage());
            result.setCode(HttpStatus.NOT_ACCEPTABLE.value());
            return result;
        }
/* 
        try {
            
            

            // we may add a club on the fly
            List<Club> clubs = clubsService.getClubs();

            Map<Race, List<RaceEvent>> eventsPerRace = new HashMap<>();
            Map<Race, AtomicInteger> raceCurrentRaceNumbers = new HashMap<>();
            for (Race race : races) {
                List<RaceEvent> raceEvents = eventService.getRaceEventsForRace(race);
                eventsPerRace.put(race, raceEvents);
                raceCurrentRaceNumbers.put(race, new AtomicInteger(0));
            }

            // skip 1st line as it is the header
            List<Entry> entries = new ArrayList<>();
            for (int i = 1; i < contents.size(); i++) {
                String line = fileContents.get(i);
                LOG.debug(line);
                Entry entry = new Entry();

                // use i as number
                FileProcessingResult result = getEntry(line, raceCurrentRaceNumbers, entry, races, eventsPerRace, events, clubs);
                if (result.getCode() != 200) {
                    return result;
                }

                // do we already have a name with the same number
                for (Entry existing : entries) {
                    if (existing.getRace().getId() == entry.getRace().getId() && existing.getNumber() == entry.getNumber()) {
                        LOG.warn("Got duplicate number for " + entry.getNumber());
                        result.setCode(505);
                        result.setData(entry.getNumber().toString());
                        return result;
                    }
                }
                entries.add(entry);
            }

            for (Race race : races) {
                LOG.debug("Wiping existing entries for race " + race.getName());
                entriesService.deleteEntriesForRace(race);
            }

            LOG.debug("Adding new entries");
            for (Entry entry : entries) {
                entriesService.addEntry(entry, null);
            }

            FileProcessingResult result = new FileProcessingResult();
            result.setCode(200);

            return result;

        } catch (Exception e) {
            LOG.warn("Got dao exception", e);
            throw new ServiceException(e);
        }
         */
    }

    private Race getRaceForName(List<Race> races, String name) {
        return races.stream().filter((r) -> r.getName().equalsIgnoreCase(name)).findFirst().orElse(null);
    }

    private Event getEventForName(List<Event> events, String name) {
        return events.stream().filter((e) -> e.getName().equalsIgnoreCase(name)).findFirst().orElse(null);
    }

    private Club getClubForName(List<Club> clubs, String name) {
        return clubs.stream().filter((c) -> c.getClubName().equalsIgnoreCase(name)).findFirst().orElse(null);
    }

    private FileProcessingResult getEntry(String line, Map<Race, AtomicInteger> raceCurrentRaceNumbers, Entry entry, List<Race> races, Map<Race, List<RaceEvent>> eventsPerRace, List<Event> events, List<Club> clubs) {
        FileProcessingResult result = new FileProcessingResult();
        result.setCode(200);

        boolean haveEndOfEventName = false;
        // split will be awkward here use search by char
        for (int i = 1; i < line.length(); i++){
            char c = line.charAt(i);

            if (c == '"') {
                if ( !haveEndOfEventName) {
                    haveEndOfEventName = true;
                    String eventData = line.substring(1, i - 1);
                    // split on ,
                    String[] eventDataSplit = eventData.split(",");
                    if (eventDataSplit != null) {
                        String event = eventDataSplit[0].trim();
                        String startTime = eventDataSplit[2].trim();

                        if (event == null) {
                            LOG.warn("Have null for event ");
                            result.setCode(502);
                            return result;
                        }

                        if (startTime == null) {
                            LOG.warn("Have null for start time ");
                            result.setCode(502);
                            return result;
                        }

                        Event entryEvent = null;
                        for (Event e : events) {
                            if (e.getName().equals(event)) {
                                entryEvent = e;
                                break;
                            }
                        }
                        // do we have the event
                        if (entryEvent == null) {
                            LOG.warn("Failed to find event " + event);
                            result.setCode(502);
                            result.setData(event);
                            return result;
                        }

                        // is the time listed in our list
                        Race entryRace = null;
                        for (Race race : races) {
                            if (race.getStartTime().equals(startTime)) {
                                entryRace = race;
                                break;
                            }
                        }

                        if (entryRace == null) {
                            LOG.warn("Failed to find race matching start time " + startTime);
                            result.setCode(502);
                            result.setData(event);
                            return result;
                        }

                        entry.setRace(entryRace);
                        entry.setEvent(entryEvent);

                        // are in the correct race, if not we are time only
                        List<RaceEvent> raceEvents = eventsPerRace.get(entryRace);
                        boolean found = false;
                        for (RaceEvent raceEvent : raceEvents) {
                            if (raceEvent.getEvent().equals(entryEvent)) {
                                found = true;
                                break;
                            }
                        }
                        if (!found) {
                            LOG.debug("Setting time only as not found in expected event");
                            entry.setTimeOnly(true);
                        } else {
                            entry.setTimeOnly(false);
                        }

                        Integer number = raceCurrentRaceNumbers.get(entryRace).incrementAndGet();
                        LOG.debug("Number is " + number);
                        entry.setNumber(number);
                        entry.setRaceOrder(number);

                        // shift over the draw position
                        boolean haveStartOfDrawPosition = false;
                        for (; i < line.length(); i++){
                            c = line.charAt(i);
                            if (c == ',') {
                                if ( !haveStartOfDrawPosition) {
                                    haveStartOfDrawPosition = true;
                                } else {
                                    break;
                                }
                            }
                        }

                        i++;
                        c = line.charAt(i);
                        if (c == '"') {
//                            "Mens Masters 1X, Fixed  , 13:30  , 14:30 ",2,"Galway, Heaney, S (d)"
                            // have sculler
                            // read rest of the string
                            int startPoint = i + 1;
                            boolean haveClub = false;
                            for (; i < line.length(); i++) {
                                c = line.charAt(i);

                                if (c == ',' && !haveClub) {
                                    String clubsValue = line.substring(startPoint, i);
                                    Set<Club> clubSet = new HashSet<>();
                                    haveClub = true;

                                    // do we have composites ?
                                    String[] clubsSplit = clubsValue.split(" / ");
                                    for (String clubInSplit : clubsSplit) {
                                        clubInSplit = clubInSplit.trim();
                                        boolean isClubKnown  = false;
                                        for (Club club : clubs) {
                                            if (club.getClubName().equals(clubInSplit)) {
                                                isClubKnown = true;
                                                clubSet.add(club);
                                                break;
                                            }
                                        }

                                        if ( !isClubKnown) {
                                            LOG.warn("Failed to find club " + clubInSplit);
                                            result.setCode(504);
                                            result.setData(clubInSplit);
                                            return result;
                                        }
                                    }

                                    entry.setClubs(clubSet);

                                    i++;
                                    // now handle name, masters
                                    boolean haveMasters = false;
                                    int mastersStartPoint = i;
                                    int nameStartPoint = i;
                                    for (; i < line.length(); i++) {
                                        c = line.charAt(i);
                                        if (c == '(') {
                                            mastersStartPoint = i + 1;
                                            haveMasters = true;
                                        }
                                        if (c == ')' && haveMasters) {
                                            String weighting = line.substring(mastersStartPoint, i);
                                            entry.setWeighting(weighting);
                                            break;
                                        }
                                    }

                                    if (haveMasters) {
                                        String name = line.substring(nameStartPoint, mastersStartPoint - 1);
                                        name = name.trim();
                                        entry.setName(name);
                                    } else {
                                        String name = line.substring(nameStartPoint, line.length() - 1);
                                        name = name.replace("\"", "");
                                        name = name.trim();
                                        entry.setName(name);
                                    }
                                    break;
                                }
                            }
                        } else {
                            boolean foundClub = false;
                            int startPoint = i;
                            // every thing up to a double space or ( is a club

                            // a name will be 2 spaces after club followed by (...) for masters, if there is no name then will have (...) or nothing
                            for (; i < line.length(); i++) {
                                c = line.charAt(i);

                                if (c == ' ' && (line.charAt(i + 1) == ' ' || line.charAt(i + 1) == '(')) {
                                    String clubsValue = line.substring(startPoint, i);
                                    Set<Club> clubSet = new HashSet<>();
                                    foundClub = true;
                                    // do we have composites ?
                                    String[] clubsSplit = clubsValue.split(" / ");
                                    if (clubsSplit.length > 1) {
                                        LOG.debug("have composite name " + clubsValue);
                                    }
                                    for (String clubInSplit : clubsSplit) {
                                        clubInSplit = clubInSplit.trim();
                                        boolean isClubKnown = false;
                                        for (Club club : clubs) {
                                            if (club.getClubName().equals(clubInSplit)) {
                                                isClubKnown = true;
                                                clubSet.add(club);
                                                break;
                                            }
                                        }

                                        if (!isClubKnown) {
                                            LOG.warn("Failed to find club " + clubInSplit);
                                            result.setCode(504);
                                            result.setData(clubInSplit);
                                            return result;
                                        }
                                    }

                                    entry.setClubs(clubSet);
                                    break;
                                }
                            }

                            if ( !foundClub) {
                                String clubsValue = line.substring(startPoint, line.length());
                                clubsValue = clubsValue.trim();
                                Set<Club> clubSet = new HashSet<>();
                                foundClub = true;
                                // do we have composites ?
                                String[] clubsSplit = clubsValue.split(" / ");
                                if (clubsSplit.length > 1) {
                                    LOG.debug("have composite name " + clubsValue);
                                }
                                for (String clubInSplit : clubsSplit) {
                                    clubInSplit = clubInSplit.trim();
                                    boolean isClubKnown = false;
                                    for (Club club : clubs) {
                                        if (club.getClubName().equals(clubInSplit)) {
                                            isClubKnown = true;
                                            clubSet.add(club);
                                            break;
                                        }
                                    }

                                    if (!isClubKnown) {
                                        LOG.warn("Failed to find club " + clubInSplit);
                                        result.setCode(504);
                                        result.setData(clubInSplit);
                                        return result;
                                    }
                                }

                                entry.setClubs(clubSet);
                            }
                        }

                        if (i < line.length()) {
                            i++;
                            if (line.charAt(i) == ' ') {
                                // name
                                boolean foundMasters = false;
                                int nameStartPoint = i;
                                for (; i < line.length(); i++) {
                                    c = line.charAt(i);

                                    // with masters
                                    if (line.charAt(i) == '(') {
                                        String name = line.substring(nameStartPoint, i - 1);
                                        name = name.trim();
                                        entry.setName(name);
                                        foundMasters = true;

                                        int mastersStartPoint = i + 1;
                                        i++;
                                        for (; i < line.length(); i++) {
                                            c = line.charAt(i);
                                            if (c == ')') {
                                                String weighting = line.substring(mastersStartPoint, i);
                                                entry.setWeighting(weighting);
                                                break;
                                            }
                                        }
                                        break;
                                    }
                                }

                                if ( !foundMasters) {
                                    String name = line.substring(nameStartPoint, line.length());
                                    name = name.trim();
                                    entry.setName(name);
                                }

                            } else if (line.charAt(i) == '(') {
                                // no name but masters
                                int mastersStartPoint = i + 1;
                                for (; i < line.length(); i++) {
                                    c = line.charAt(i);
                                    if (c == ')') {
                                        String weighting = line.substring(mastersStartPoint, i);
                                        entry.setWeighting(weighting);
                                        break;
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        if ( !entry.getEvent().getName().contains("Masters") && entry.getWeighting() != null) {
            LOG.warn("Got weighting for non-masters event " + entry.getEvent().getName());
            result.setCode(503);
            result.setData(entry.getEvent().getName());
            return result;
        }
        return result;
    }

    private List<EntryLine> getEntriesForList(InputStream data) throws Exception {
        BOMInputStream bomInputStream = BOMInputStream.builder().setInputStream(data).get();
        Reader reader = new InputStreamReader(bomInputStream, StandardCharsets.UTF_8);
        return new CsvToBeanBuilder<EntryLine>(reader)
            .withType(EntryLine.class)
            .build()
            .parse();
    }

    private Event getEvent(String suppliedEvent, List<Event> events) {
        Event event = null;
        for (Event e : events) {
            if (e.getName().equals(suppliedEvent)) {
                event = e;
                break;
            }
        }
        return event;
    }

    private Club getClub(String suppliedClub, List<Club> clubs) {
        Club club = null;
        for (Club c : clubs) {
            if (c.getClubName().equals(suppliedClub)) {
                club = c;
                break;
            }
        }
        return club;
    }

    private FileProcessingResult processEntryLine(String line, Entry entry, List<Event> events, List<Club> clubs)  {
        FileProcessingResult result = new FileProcessingResult();
        result.setCode(200);
        // split on tabs
        String[] tabbed = line.split("\\t");
        LOG.debug("Have tabbed " + tabbed.length + " parts to entry " + Arrays.toString(tabbed));
        // 1st is number
        String number = tabbed[0];
        number = number.replace(",", "").trim();
        entry.setNumber(Integer.valueOf(number));
        entry.setRaceOrder(Integer.valueOf(number));

        StringBuffer buildRemains = new StringBuffer();

        // 2nd is event
        String suppliedEvent = tabbed[1].trim();
        // do we have text beyond the comma then we are missing the tab - sigh
        if (suppliedEvent.contains(",")) {
            int position = suppliedEvent.indexOf(",");
            if (suppliedEvent.length() > position + 1) {
                LOG.debug("Missing tab");
                String missedTab = suppliedEvent.substring(position + 1);
                buildRemains.append(missedTab);
                String correctedEvent = suppliedEvent.substring(0, position + 1);
                suppliedEvent = correctedEvent;
            }
        }
        suppliedEvent = suppliedEvent.replace(",", "").trim();
        Event event = getEvent(suppliedEvent, events);
        if (event == null) {
            LOG.warn("Failed to find event " + suppliedEvent);
            result.setCode(502);
            result.setData(suppliedEvent);
            return result;
        }
        entry.setEvent(event);

        // 3rd clubs and pot luck after that

        for (int i = 2; i < tabbed.length; i++) {
            if ( !tabbed[i].equals("-")) {
                buildRemains.append(tabbed[i]);
            }
        }
        String remains = buildRemains.toString();

        // clean up the weighting as it is all over the place
        // Masters may contain () all others should not
        if ( !event.getName().contains("Masters") && remains.contains("(")) {
            LOG.warn("Got weighting for non-masters event " + event.getName());
            result.setCode(503);
            result.setData(event.getName());
            return result;
        }

        if (event.getName().contains("Masters") && remains.contains("(")) {
            // extract and remove the weighting
            String weighting = remains.substring(remains.indexOf('(') + 1, remains.indexOf(')'));
            entry.setWeighting(weighting);
            LOG.debug("Have weighting " + weighting);
            remains = remains.substring(0, remains.indexOf('(')) + remains.substring(remains.indexOf(')') + 1);
        }

        // check to see if timing only
        if (remains.contains("[TIMING ONLY]")) {
            LOG.debug("timing only");
            entry.setTimeOnly(true);
            remains = remains.replace("[TIMING ONLY]", "");
        } else {
            entry.setTimeOnly(false);
        }

        // clubs will not have a comma in them
        String suppliedClubs;
        if (remains.contains(",")) {
            suppliedClubs = remains.substring(0, remains.indexOf(","));
            remains = remains.substring(remains.indexOf(",") + 1).trim();
            if (remains.contains(",")) {
                remains = remains.substring(0, remains.lastIndexOf(",")).trim();
            }
        } else {
            suppliedClubs = remains;
            remains = "";
        }
        suppliedClubs = suppliedClubs.replace(",", "").trim();
        LOG.debug("clubs " + suppliedClubs);
        String lastClub = suppliedClubs;
        Set<Club> clubSet = new HashSet<>();
        // Clubs will be be split by / for composities or end with indicator which should actually be name
        if (suppliedClubs.contains("/")) {
            LOG.debug("have composite");
            String[] clubsSplit = suppliedClubs.split("/");
            lastClub = clubsSplit[clubsSplit.length - 1];
            for (int i=0; i < clubsSplit.length - 1; i++) {
                Club club = getClub(clubsSplit[i].trim(), clubs);
                if (club == null) {
                    LOG.warn("Failed to find club " + clubsSplit[i].trim());
                    result.setCode(504);
                    result.setData(clubsSplit[i].trim());
                    return result;
                }
                clubSet.add(club);
            }
        }
        // check that the last club is not a club and a name
        Club club = getClub(lastClub.trim(), clubs);
        if (club == null) {
            // split on last space, if there is one
            if ( !lastClub.contains(" ")) {
                LOG.warn("Failed to find club " + lastClub.trim());
                result.setCode(504);
                result.setData(lastClub.trim());
                return result;
            }
            String clubName = lastClub.substring(0, lastClub.lastIndexOf(" "));
            String name = lastClub.substring(lastClub.lastIndexOf(" "));
            club = getClub(clubName.trim(), clubs);
            if (club == null) {
                LOG.warn("Failed to find club " + clubName.trim());
                result.setCode(504);
                result.setData(clubName.trim());
                return result;
            }
            LOG.debug("name is " + name.trim());
            entry.setName(name.trim());
            clubSet.add(club);
        } else {
            clubSet.add(club);
        }
        for (Club c : clubSet) {
            LOG.debug("Club " + c.getClubName());
        }
        entry.setClubs(clubSet);

        if (remains.length() > 0 && entry.getName() == null) {
            remains = remains.trim();
            if ( !remains.equals("-")) {
                LOG.debug("name is " + remains);
                entry.setName(remains);
            }
        }
        return result;
    }
}
