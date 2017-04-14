import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Params, Router, RouterStateSnapshot } from '@angular/router';

import { Observable } from 'rxjs/Observable';
import { Subscription } from 'rxjs/Subscription';

import { TranslateService } from '@ngx-translate/core';

import { Position } from '../../positions/position.model';
import { Race } from '../../races/race.model';
import { Club } from '../../clubs/club.model';
import { Entry } from '../../entries/entry.model';
import { Event } from '../../events/event.model';
import { Penalty } from '../../penalties/penalty.model';
import { Disqualification } from '../../disqualification/disqualification.model';
import { PositionTime, convertFromTimeStamp } from '../../times/postion-time.model';
import { EntryTime } from '../../times/entry-time.model';
import { EventPosition } from '../../events/event-positions.model';
import { RaceEvent } from '../../race-events/race-event.model';

import { PositionsService } from '../../positions/positions.service';
import { RacesService } from '../../races/races.service';
import { EntryTimesService } from '../../times/entry-times.service';
import { PenaltiesService } from '../../penalties/penalties.service';
import { DisqualificationService } from '../../disqualification/disqualification.service';
import { EventsService } from '../../events/events.service';
import { ClubsService } from '../../clubs/clubs.service';
import { RaceEventsService } from '../../race-events/race-events.service';
import { TimesPositions } from './time-positions.model';
import { EventsAtPositions } from './events-at-positions.model';

@Component({
    selector: 'race-results',
    styleUrls: [ './race-results.component.css' ],
    templateUrl: './race-results.component.html',
    providers: [ EntryTimesService ]
})
export class RaceResultsComponent implements OnInit, OnDestroy {
    public page: number = 1;
    public itemsPerPage: number = 10;
    public positions: Position[];
    public race: Race;
    public displayablePositions: Position[];
    public reverseNumberSort = false;
    public reverseAdjustedTimeSort = false;
    public numberFilter: string = '';
    public clubFilter: string = '';
    public eventFilter: string = '';
    public filteredEntryTimes: EntryTime[];
    public timesForRace: TimesPositions[] = new Array<TimesPositions>();
    public eventsAtPositions: EventsAtPositions[] = new Array<EventsAtPositions>();

    private pollingPeriod: number = 200000;
    private raceId: number = 0;
    private clubs: Club[] = new Array<Club>();
    private events: Event[] = new Array<Event>();
    private races: Race[];
    private raceEvents: RaceEvent[];
    private raceEventsForRace: RaceEvent[];
    private entryTimes: EntryTime[];
    private disqualifiedText: string;
    private penaltyText: string;
    private penalties: Penalty[];
    private disqualifications: Disqualification[];
    private polling: Subscription;
    private positionsSubscription: Subscription;
    private racesSubscription: Subscription;
    private penaltiesSubscription: Subscription;
    private clubsSubscription: Subscription;
    private eventsSubscription: Subscription;
    private raceEventsSubscription: Subscription;
    private disqualificationsSubscription: Subscription;
    private translateDisqualifiedSubscription: Subscription;
    private translatePenaltySubscription: Subscription;
    private isDefaulting = false;

    constructor(
        private route: ActivatedRoute,
        private router: Router,
        private translate: TranslateService,
        private racesService: RacesService,
        private raceEventsService: RaceEventsService,
        private positionsService: PositionsService,
        private entryTimesService: EntryTimesService,
        private clubsService: ClubsService,
        private eventsService: EventsService,
        private penaltiesService: PenaltiesService,
        private disqualificationService: DisqualificationService
    ) {}

    public ngOnInit() {
        this.positionsSubscription = this.positionsService.getPositions()
            .subscribe((positions: Position[]) => {
                this.positions = positions;
                this.displayablePositions = positions.slice();
                this.displayablePositions.shift();
        });

        this.racesSubscription = this.racesService.getRaces().subscribe((races: Race[]) => {
            this.races = races;
            if (this.raceId) {
                this.setRaceForRaceId(this.raceId);
            }
        });

        this.penaltiesSubscription =
            this.penaltiesService.getPenalties().subscribe((penalties: Penalty[]) => {
            this.penalties = penalties;
        });

        this.clubsSubscription = this.clubsService.getClubs().subscribe((clubs: Club[]) => {
            this.clubs = clubs;
        });

        this.eventsSubscription = this.eventsService.getEvents().subscribe((events: Event[]) => {
            this.events = events;
            console.log(events);
            this.populateEventsAtPositionsForRace(this.race);
        });

        this.raceEventsSubscription =
            this.raceEventsService.getEvents().subscribe((events: RaceEvent[]) => {
            this.raceEvents = events;
            this.populateEventsAtPositionsForRace(this.race);
        });

        this.disqualificationsSubscription = this.disqualificationService.getDisqualifications()
            .subscribe((disqualifications: Disqualification[]) => {
                this.disqualifications = disqualifications;
        });

        this.route.params.subscribe((params: Params) => {
            this.raceId = +params['raceId'];
            this.setRaceForRaceId(this.raceId);
            this.numberFilter = '';
            this.clubFilter = '';
            this.eventFilter = '';
            let url: string = this.router.routerState.snapshot.url;
            let count = (url.match(/\//g) || []).length;
            if (count === 2) {
                this.isDefaulting = true;
            }
        });

        this.translateDisqualifiedSubscription =
            this.translate.get('DISQUALIFIED').subscribe((res: string) => {
            this.disqualifiedText = res;
        });
        this.translatePenaltySubscription =
            this.translate.get('PENALTY').subscribe((res: string) => {
            this.penaltyText = res;
        });

        this.polling = Observable.interval(this.pollingPeriod).map(() => {
            console.log('polling');
            this.racesService.refresh();
            this.positionsService.refresh();
            this.penaltiesService.refresh();
            this.disqualificationService.refresh();
            if (this.race) {
                this.entryTimesService.refreshForRace(this.race);
            }
        }).subscribe();
    }

    public ngOnDestroy() {
        this.polling.unsubscribe();
        this.positionsSubscription.unsubscribe();
        this.racesSubscription.unsubscribe();
        this.clubsSubscription.unsubscribe();
        this.penaltiesSubscription.unsubscribe();
        this.eventsSubscription.unsubscribe();
        this.disqualificationsSubscription.unsubscribe();
        this.translateDisqualifiedSubscription.unsubscribe();
        this.translatePenaltySubscription.unsubscribe();
        this.raceEventsSubscription.unsubscribe();
    }

    public onKey(value: string, field: string) {
        if (field === 'number') {
            this.numberFilter = value;
        }
        if (field === 'club') {
            this.clubFilter = value;
        }
        if (field === 'event') {
            this.eventFilter = value;
        }
        this.filteredEntryTimes = this.filterEntries();
    }

    public sortByNumber(direction: string): void {
        this.reverseNumberSort = !this.reverseNumberSort;
        this.filteredEntryTimes.sort((e1, e2) => {
            let order: number;
            if (e1.entry.number < e2.entry.number) {
                order = -1;

            } else if (e1.entry.number === e2.entry.number) {
                order = 0;

            } else if (e1.entry.number > e2.entry.number) {
                order = 1;
            }

            if (direction === 'up') {
                order = order * -1;
            }

            return order;
        });
    }

    public sortByAdjustedTime(direction: string): void {
        this.reverseNumberSort = !this.reverseNumberSort;
    }

    public getTimeForEntry(entryTime: EntryTime, position: Position): string {
        if (this.isDisqualified(entryTime)) {
            return null;
        }

        if (entryTime.times.length > 0) {
            let actualStartPoint: PositionTime = entryTime.times
                .filter((positionTime: PositionTime) => positionTime.startPoint === true)
                .shift();
            let eventStartPoint: PositionTime = entryTime.times
                .filter((positionTime: PositionTime) => positionTime.position
                    === entryTime.entry.event)
                .shift();
            let time: PositionTime = entryTime.times
                .filter((positionTime: PositionTime) => positionTime.position === position.id)
                .shift();
            if (time && actualStartPoint) {
                return convertFromTimeStamp(time.time - actualStartPoint.time);
            }
        }
        return null;
    }

    public getAdjustedTimeForEntry(entryTime: EntryTime): string {
        if (this.isDisqualified(entryTime)) {
            return null;
        }
        if (entryTime.times.length > 0) {
            let actualStartPoint: PositionTime = entryTime.times
                .filter((positionTime: PositionTime) => positionTime.startPoint === true)
                .shift();
            let eventStartPoint: PositionTime = entryTime.times
                .filter((positionTime: PositionTime) => positionTime.position
                    === entryTime.entry.event)
                .shift();
            let eventFinishPoint: PositionTime = entryTime.times
                .filter((positionTime: PositionTime) => positionTime.position
                     === entryTime.entry.event)
                .shift();
            if (eventFinishPoint && actualStartPoint) {
                // apply penalties
                let penalties: number = this.getPenaltiesForEntry(entryTime);
                return convertFromTimeStamp(
                    eventFinishPoint.time - actualStartPoint.time + penalties);
            }
        }
        return null;
    }

    public getCommentsForEntry(entryTime: EntryTime): string {
        if (this.isDisqualified(entryTime)) {
           return this.disqualifiedText;
        }
        if (this.getPenaltiesForEntry(entryTime) > 0) {
           return this.penaltyText;
        }
        return null;
    }

    public getEventNameForEntry(entry: Entry): string {
        let eventName: string;
        let event: Event = this.getEventForId(entry.event);
        if (event) {
            eventName = event.name;
        }
        return eventName;
    }

    public getClubNamesForEntry(entry: Entry): string {
        let clubs: string;
        for (let clubId of entry.clubs) {
            let club: Club = this.getClubForId(clubId);
            if (club) {
                if (!clubs) {
                    clubs = club.clubName;
                } else {
                    clubs = clubs.concat(' / ');
                    clubs = clubs.concat(club.clubName);
                }
            }
        }
        return clubs;
    }

    public downloadResultsByTime(race: Race): void {
        window.open('/tiatus/results/' + race.name + '_ByTime.pdf', '_blank');
    }

    public downloadResultsByEvent(race: Race): void {
        window.open('/tiatus/results/' + race.name + '_ByEvent.pdf', '_blank');
    }

    private populateEventsAtPositionsForRace(race: Race): void {
        console.log('get eps');
        if (this.race) {
            this.raceEventsForRace = this.raceEventsService.getEventsForRace(this.race);
            console.log(this.raceEventsForRace);
            if (this.raceEventsForRace) {
                this.eventsAtPositions = new Array<EventsAtPositions>();
                for (let raceEvent of this.raceEventsForRace) {
                    let event: Event = this.getEventForId(raceEvent.event);
                    if (event === null) {
                        continue;
                    }
                    let finish: Position;
                    let start: Position;
                    if (event.positions.length > 1) {
                        start = this.positionsService.getPositionForId(event.positions[0].position);
                        finish = this.positionsService.getPositionForId
                            (event.positions[event.positions.length - 1].position);
                    }

                    if (this.eventsAtPositions.length === 0) {
                        let eventsAtPosition: EventsAtPositions = new EventsAtPositions();
                        eventsAtPosition.events.push(event);
                        eventsAtPosition.finish = finish;
                        eventsAtPosition.start = start;
                        this.eventsAtPositions.push(eventsAtPosition);
                    } else {
                        let found: boolean = false;
                        for (let ep of this.eventsAtPositions) {
                            if (ep.finish && finish && ep.finish.id === finish.id
                                && ep.start && start && ep.start.id === start.id) {
                                ep.events.push(event);
                                found = true;
                                break;
                            }
                        }
                        if (!found) {
                            let eventsAtPosition: EventsAtPositions = new EventsAtPositions();
                            eventsAtPosition.events.push(event);
                            eventsAtPosition.finish = finish;
                            eventsAtPosition.start = start;
                            this.eventsAtPositions.push(eventsAtPosition);
                        }
                    }
                }

                console.log(this.eventsAtPositions);
                if (this.isDefaulting) {
                    if (this.eventsAtPositions.length > 0) {
                        let eventsAtPosition: EventsAtPositions = this.eventsAtPositions[0];
                        if (eventsAtPosition.start !== null && eventsAtPosition.finish !== null) {
                            console.log('navigating');
                            let x = this.router.navigate([
                                'race',
                                this.race.id,
                                eventsAtPosition.start.name,
                                eventsAtPosition.finish.name
                            ]);
                        }
                    }
                }
            }
        }
    }

    private getEventForId(eventId: number): Event {
        for (let event of this.events) {
            if (event.id === eventId) {
                return event;
            }
        }
        return null;
    }

    private getClubForId(clubId: number): Club {
        for (let club of this.clubs) {
            if (club.id === clubId) {
                return club;
            }
        }
        return null;
    }

    private isDisqualified(entry: EntryTime): boolean {
        if (this.disqualifications) {
            let disqualifed: Disqualification = this.disqualifications
                .filter((disqualification: Disqualification) =>
                    disqualification.entry === entry.entry.id).shift();
            if (disqualifed) {
                return true;
            }
        }
        return false;
    }

    private getPenaltiesForEntry(entryTime: EntryTime): number {
        if (this.isDisqualified(entryTime)) {
            return 0;
        }

        return this.getTimeForPenaltes(entryTime);
    }

    private getTimeForPenaltes(entryTime: EntryTime): number {
        let penalties: Penalty[];
        if (this.penalties) {
            penalties = this.penalties.filter((penalty: Penalty) =>
                penalty.entry === entryTime.entry.id);
        }

        if (penalties) {
            let total: number = 0;
            penalties.map((penalty: Penalty) => total += penalty.time);
            return total;
        }
        return 0;
    }

    private getTime(entryTime: EntryTime): number {
        if (entryTime.times.length > 0) {
            let actualStartPoint: PositionTime = entryTime.times
                .filter((positionTime: PositionTime) => positionTime.startPoint === true)
                .shift();
            let eventStartPoint: PositionTime = entryTime.times
                .filter((positionTime: PositionTime) => positionTime.position
                    === entryTime.entry.event)
                .shift();
            let eventFinishPoint: PositionTime = entryTime.times
                .filter((positionTime: PositionTime) => positionTime.position
                        === entryTime.entry.event)
                .shift();
            if (eventFinishPoint && actualStartPoint) {
                return eventFinishPoint.time - actualStartPoint.time;
            }
        }

        return 0;
    }

    private getTimesForRace(race: Race): void {
        if (race) {
            console.log('Get times for race ' + race.id);
            this.populateEventsAtPositionsForRace(race);
            this.entryTimesService.getEntriesForRace(race)
                .subscribe((data: EntryTime[]) => {
                    console.log(data);
                    this.entryTimes = data;
                    this.filteredEntryTimes = this.entryTimes;
                    this.filteredEntryTimes = this.filterEntries();
            });
        }
    }

    private setRaceForRaceId(raceId: number): void {
        if (this.races) {
            this.race = this.racesService.getRaceForId(this.raceId);
            this.getTimesForRace(this.race);
        }
    }

    private filterEntries(): EntryTime[] {
        let filteredEntries: EntryTime[] = this.entryTimes;
        console.log('filter');
        console.log(filteredEntries.length);
        if (filteredEntries.length > 0) {
            this.timesForRace = new Array<TimesPositions>();
            if (this.numberFilter) {
                console.log(this.numberFilter);
                filteredEntries = this.filterNumbers(filteredEntries, this.numberFilter);
            }
            if (this.clubFilter) {
                filteredEntries = this.filterClubs(filteredEntries, this.clubFilter);
            }
            if (this.eventFilter) {
                filteredEntries = this.filterEvents(filteredEntries, this.eventFilter);
            }

            for (let entry of filteredEntries) {
                this.addEntryToTimes(entry);
            }
            console.log(this.timesForRace);
        }
        return filteredEntries;
    }

    private createTimesForRaceEntry(
            entryTime: EntryTime,
            start: Position,
            finish: Position
        ): TimesPositions {
        let timesPositions: TimesPositions = new TimesPositions();
        timesPositions.finish = finish;
        timesPositions.start = start;
        timesPositions.times = new Array<EntryTime>();
        return timesPositions;
    }

    private addEntryToTimes(entryTime: EntryTime): void {
        let event: Event = this.eventsService.getEventForId(entryTime.entry.event);
        let eventPositions: EventPosition[] = event.positions;
        let finish: Position = null;
        let start: Position = null;
        if (eventPositions.length > 1) {
            finish =
                this.positionsService.getPositionForId
                    (eventPositions[eventPositions.length - 1].position);
            start =
                this.positionsService.getPositionForId(eventPositions[0].position);
        }
        if (this.timesForRace.length > 0) {
            let found: boolean = false;
            for (let et of this.timesForRace) {
                if (et.finish === null && finish === null && et.start === null && start === null) {
                    found = true;
                    et.times.push(entryTime);
                    break;
                }
                if (et.finish !== null && finish !== null && et.start !== null && start !== null
                    && et.finish.id === finish.id && et.start.id === start.id) {
                    found = true;
                    et.times.push(entryTime);
                    break;
                }
            }
            if (!found) {
                let timesPositions: TimesPositions =
                this.createTimesForRaceEntry(entryTime, start, finish);
                this.timesForRace.push(timesPositions);
            }
        } else {
            let timesPositions: TimesPositions =
                this.createTimesForRaceEntry(entryTime, start, finish);
            this.timesForRace.push(timesPositions);
        }
    }

    private filterNumbers(entryTimes: EntryTime[], value: string): EntryTime[] {
        return entryTimes.filter((entryTime: EntryTime) =>
            entryTime.entry.number.toString().includes(value));
    }

    private filterClubs(entryTimes: EntryTime[], value: string): EntryTime[] {
        return entryTimes.filter((entryTime: EntryTime) => {
            for (let clubId of entryTime.entry.clubs) {
                let club: Club = this.getClubForId(clubId);
                if (club) {
                    if (club.clubName.includes(value)) {
                        return entryTime;
                    }
                }
            }
        });

    }

    private filterEvents(entryTimes: EntryTime[], value: string): EntryTime[] {
        return entryTimes.filter((entryTime: EntryTime) => {
            let event: Event = this.getEventForId(entryTime.entry.event);
            if (event) {
                if (event.name.includes(value)) {
                    return entryTime;
                }
            }
        });
    }
}
