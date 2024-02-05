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

import { ResultsPositionsService } from '../positions/positions.service';
import { ResultsRacesService } from '../races/races.service';
import { EntriesService } from '../../entries/entries.service';
import { EntryTimesService } from '../../times/entry-times.service';
import { ResultsEventsService } from '../events/events.service';
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

    private raceId: number = 0;
    private events: Event[] = new Array<Event>();
    private races: Race[];
    private entries: Entry[];
    private entriesForRace: Entry[];
    private entryTimes: EntryTime[];
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
        private racesService: ResultsRacesService,
        private positionsService: ResultsPositionsService,
        private entryTimesService: EntryTimesService,
        private entriesService: EntriesService,
        private eventsService: ResultsEventsService
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
            const url: string = this.router.routerState.snapshot.url;
            const count = (url.match(/\//g) || []).length;
            if (count === 2) {
                this.isDefaulting = true;
            }
        });
    }

    public ngOnDestroy() {
        this.positionsSubscription.unsubscribe();
        this.racesSubscription.unsubscribe();
        this.eventsSubscription.unsubscribe();
        if (this.raceEntriesSubscription) {
            this.raceEntriesSubscription.unsubscribe();
        }
    }

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
                    if (raceEntries === undefined) {
                        // call may not have completed yet
                        return;
                    }

                    this.entriesForRace = raceEntries;
                    this.eventsAtPositions = new Array<EventsAtPositions>();

                    for (const entry of raceEntries) {
                        const event: Event = this.eventsService.getEventForId(entry.event);
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
                            const eventsAtPosition: EventsAtPositions = new EventsAtPositions();
                            eventsAtPosition.events.push(event);
                            eventsAtPosition.finish = finish;
                            eventsAtPosition.start = start;
                            this.eventsAtPositions.push(eventsAtPosition);
                        } else {
                            let found: boolean = false;
                            for (const ep of this.eventsAtPositions) {
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
                                    let newEvent: boolean = true;
                                    for (const epEvent of ep.events) {
                                        if (epEvent.id === event.id) {
                                            newEvent = false;
                                            break;
                                        }
                                    }
                                    if (newEvent) {
                                        ep.events.push(event);
                                    }
                                    found = true;
                                    break;
                                }
                            }
                            if (!found) {
                                const eventsAtPosition: EventsAtPositions = new EventsAtPositions();
                                eventsAtPosition.events.push(event);
                                eventsAtPosition.finish = finish;
                                eventsAtPosition.start = start;
                                this.eventsAtPositions.push(eventsAtPosition);
                            }
                        }
                    }

                    if (this.isDefaulting) {
                        if (this.eventsAtPositions.length > 0) {
                            const eventsAtPosition: EventsAtPositions = this.eventsAtPositions[0];
                            if (eventsAtPosition.start !== null
                                && eventsAtPosition.finish !== null) {
                                this.router.navigate([
                                    'race',
                                    this.race.id,
                                    eventsAtPosition.start.name,
                                    eventsAtPosition.finish.name
                                ]);
                            } else {
                                console.log("Cannot navigate to " + this.race.id);
                            }
                        }
                    }
            });
        }
    }

    private setRaceForRaceId(raceId: number): void {
        if (this.races) {
            this.race = this.racesService.getRaceForId(this.raceId);
            this.populateEventsAtPositionsForRace(this.race);
        }
    }

}
