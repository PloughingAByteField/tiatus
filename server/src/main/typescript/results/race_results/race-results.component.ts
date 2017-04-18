import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Params, Router, RouterStateSnapshot } from '@angular/router';

import { Observable } from 'rxjs/Observable';
import { Subscription } from 'rxjs/Subscription';

import { Position } from '../../positions/position.model';
import { Race } from '../../races/race.model';
import { Entry } from '../../entries/entry.model';
import { Event } from '../../events/event.model';
import { PositionTime, convertFromTimeStamp } from '../../times/postion-time.model';
import { EntryTime } from '../../times/entry-time.model';
import { EventPosition } from '../../events/event-positions.model';

import { PositionsService } from '../../positions/positions.service';
import { RacesService } from '../../races/races.service';
import { EntriesService } from '../../entries/entries.service';
import { EntryTimesService } from '../../times/entry-times.service';
import { EventsService } from '../../events/events.service';
import { TimesPositions } from './time-positions.model';
import { EventsAtPositions } from './events-at-positions.model';

@Component({
    selector: 'race-results',
    styleUrls: [ './race-results.component.css' ],
    templateUrl: './race-results.component.html',
    providers: [ EntryTimesService ]
})
export class RaceResultsComponent implements OnInit, OnDestroy {
    public positions: Position[];
    public race: Race;
    public reverseNumberSort = false;
    public reverseAdjustedTimeSort = false;

    public eventsAtPositions: EventsAtPositions[] = new Array<EventsAtPositions>();

    private pollingPeriod: number = 200000;
    private raceId: number = 0;
    private events: Event[] = new Array<Event>();
    private races: Race[];
    private entries: Entry[];
    private entriesForRace: Entry[];
    private entryTimes: EntryTime[];
    private polling: Subscription;
    private positionsSubscription: Subscription;
    private racesSubscription: Subscription;
    private penaltiesSubscription: Subscription;
    private clubsSubscription: Subscription;
    private eventsSubscription: Subscription;
    private entriesSubscription: Subscription;
    private raceEntriesSubscription: Subscription;
    private disqualificationsSubscription: Subscription;
    private translateDisqualifiedSubscription: Subscription;
    private translatePenaltySubscription: Subscription;
    private isDefaulting = false;

    constructor(
        private route: ActivatedRoute,
        private router: Router,
        private racesService: RacesService,
        private positionsService: PositionsService,
        private entryTimesService: EntryTimesService,
        private entriesService: EntriesService,
        private eventsService: EventsService
    ) {}

    public ngOnInit() {
        this.positionsSubscription = this.positionsService.getPositions()
            .subscribe((positions: Position[]) => {
                this.positions = positions;
        });

        this.racesSubscription = this.racesService.getRaces().subscribe((races: Race[]) => {
            this.races = races;
            if (this.raceId) {
                this.setRaceForRaceId(this.raceId);
            }
        });

        this.eventsSubscription = this.eventsService.getEvents().subscribe((events: Event[]) => {
            this.events = events;
            this.populateEventsAtPositionsForRace(this.race);
        });

        this.entriesSubscription = this.entriesService.getEntries().subscribe
            ((entries: Entry[]) => {
            this.entries = entries;
            this.populateEventsAtPositionsForRace(this.race);
        });

        this.route.params.subscribe((params: Params) => {
            this.raceId = +params['raceId'];
            this.setRaceForRaceId(this.raceId);
            let url: string = this.router.routerState.snapshot.url;
            let count = (url.match(/\//g) || []).length;
            if (count === 2) {
                this.isDefaulting = true;
            }
        });

        // this.polling = Observable.interval(this.pollingPeriod).map(() => {
        //     console.log('polling');
        //     this.racesService.refresh();
        //     this.positionsService.refresh();
        //     this.penaltiesService.refresh();
        //     this.disqualificationService.refresh();
        //     if (this.race) {
        //         this.entryTimesService.refreshForRace(this.race);
        //     }
        // }).subscribe();
    }

    public ngOnDestroy() {
        // this.polling.unsubscribe();
        this.positionsSubscription.unsubscribe();
        this.racesSubscription.unsubscribe();
        this.eventsSubscription.unsubscribe();
        // this.raceEventsSubscription.unsubscribe();
        if (this.raceEntriesSubscription) {
            this.raceEntriesSubscription.unsubscribe();
        }
    }

    // public sortByNumber(direction: string): void {
    //     this.reverseNumberSort = !this.reverseNumberSort;
    //     this.filteredEntryTimes.sort((e1, e2) => {
    //         let order: number;
    //         if (e1.entry.number < e2.entry.number) {
    //             order = -1;

    //         } else if (e1.entry.number === e2.entry.number) {
    //             order = 0;

    //         } else if (e1.entry.number > e2.entry.number) {
    //             order = 1;
    //         }

    //         if (direction === 'up') {
    //             order = order * -1;
    //         }

    //         return order;
    //     });
    // }

    // public sortByAdjustedTime(direction: string): void {
    //     this.reverseNumberSort = !this.reverseNumberSort;
    // }

    public downloadResultsByTime(race: Race): void {
        window.open('/tiatus/results/' + race.name + '_ByTime.pdf', '_blank');
    }

    public downloadResultsByEvent(race: Race): void {
        window.open('/tiatus/results/' + race.name + '_ByEvent.pdf', '_blank');
    }

    private populateEventsAtPositionsForRace(race: Race): void {
        if (this.race) {
            this.raceEntriesSubscription = this.entriesService.getEntriesForRace(this.race)
                .subscribe((raceEntries: Entry[]) => {
                    console.log(raceEntries);
                    this.entriesForRace = raceEntries;
                    this.eventsAtPositions = new Array<EventsAtPositions>();
                    for (let entry of raceEntries) {
                        let event: Event = this.eventsService.getEventForId(entry.event);
                        if (event === null) {
                            continue;
                        }
                        let finish: Position;
                        let start: Position;
                        if (event.positions.length > 1) {
                            start = this.positionsService
                                .getPositionForId(event.positions[0].position);
                            finish = this.positionsService.getPositionForId
                                (event.positions[event.positions.length - 1].position);
                        }

                        if (this.eventsAtPositions.length === 0) {
                            console.log('length 0');
                            let eventsAtPosition: EventsAtPositions = new EventsAtPositions();
                            eventsAtPosition.events.push(event);
                            eventsAtPosition.finish = finish;
                            eventsAtPosition.start = start;
                            this.eventsAtPositions.push(eventsAtPosition);
                        } else {
                            let found: boolean = false;
                            for (let ep of this.eventsAtPositions) {
                                console.log(ep);
                                let matchingPosition: boolean = false;
                                if (ep.finish && finish && ep.finish.id === finish.id
                                    && ep.start && start && ep.start.id === start.id) {
                                    matchingPosition = true;
                                }
                                if (ep.finish === undefined && finish === undefined
                                    && ep.start === undefined && start === undefined) {
                                    matchingPosition = true;
                                }
                                if (matchingPosition) {
                                    console.log('Matching positions');
                                    let newEvent: boolean = true;
                                    for (let epEvent of ep.events) {
                                        if (epEvent.id === event.id) {
                                            newEvent = false;
                                            break;
                                        }
                                    }
                                    if (newEvent) {
                                        console.log(event);
                                        ep.events.push(event);
                                    }
                                    found = true;
                                    break;
                                }
                            }
                            if (!found) {
                                console.log('not found');
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
                            if (eventsAtPosition.start !== null
                                && eventsAtPosition.finish !== null) {
                                this.router.navigate([
                                    'race',
                                    this.race.id,
                                    eventsAtPosition.start.name,
                                    eventsAtPosition.finish.name
                                ]);
                            }
                        }
                    }
            });
        }
    }

    // private getTime(entryTime: EntryTime): number {
    //     if (entryTime.times.length > 0) {
    //         let actualStartPoint: PositionTime = entryTime.times
    //             .filter((positionTime: PositionTime) => positionTime.startPoint === true)
    //             .shift();
    //         let eventStartPoint: PositionTime = entryTime.times
    //             .filter((positionTime: PositionTime) => positionTime.position
    //                 === entryTime.entry.event)
    //             .shift();
    //         let eventFinishPoint: PositionTime = entryTime.times
    //             .filter((positionTime: PositionTime) => positionTime.position
    //                     === entryTime.entry.event)
    //             .shift();
    //         if (eventFinishPoint && actualStartPoint) {
    //             return eventFinishPoint.time - actualStartPoint.time;
    //         }
    //     }

    //     return 0;
    // }

    // private getTimesForRace(race: Race): void {
    //     if (race) {
    //         console.log('Get times for race ' + race.id);
    //         this.populateEventsAtPositionsForRace(race);
    //         this.entryTimesService.getEntriesForRace(race)
    //             .subscribe((data: EntryTime[]) => {
    //                 console.log(data);
    //                 this.entryTimes = data;
    //                 this.filteredEntryTimes = this.entryTimes;
    //                 // this.filteredEntryTimes = this.filterEntries();
    //         });
    //     }
    // }

    private setRaceForRaceId(raceId: number): void {
        if (this.races) {
            this.race = this.racesService.getRaceForId(this.raceId);
            this.populateEventsAtPositionsForRace(this.race);
        }
    }

    // private filterEntries(): EntryTime[] {
    //     let filteredEntries: EntryTime[] = this.entryTimes;
    //     console.log('filter');
    //     console.log(filteredEntries.length);
    //     if (filteredEntries.length > 0) {
    //         this.timesForRace = new Array<TimesPositions>();
    //         if (this.numberFilter) {
    //             console.log(this.numberFilter);
    //             filteredEntries = this.filterNumbers(filteredEntries, this.numberFilter);
    //         }
    //         if (this.clubFilter) {
    //             filteredEntries = this.filterClubs(filteredEntries, this.clubFilter);
    //         }
    //         if (this.eventFilter) {
    //             filteredEntries = this.filterEvents(filteredEntries, this.eventFilter);
    //         }

    //         for (let entry of filteredEntries) {
    //             this.addEntryToTimes(entry);
    //         }
    //         console.log(this.timesForRace);
    //     }
    //     return filteredEntries;
    // }

}
