import { Component, OnInit, Input } from '@angular/core';
import { ActivatedRoute, Params } from '@angular/router';
import { Location }                 from '@angular/common';

import { Observable } from 'rxjs/Observable';

import { Race } from '../../races/race.model';
import { Club } from '../../clubs/club.model';
import { Penalty } from '../../penalties/penalty.model';
import { Entry } from '../../entries/entry.model';
import { Event } from '../../events/event.model';
import { convertToTimeStamp, convertFromTimeStamp } from '../../times/postion-time.model';

import { ClubsService } from '../../clubs/clubs.service';
import { EntriesService } from '../../entries/entries.service';
import { EventsService } from '../../events/events.service';
import { AdjudicatorRacesService } from '../races/races.service';
import { AdjudicatorHttpPenaltiesService } from './penalties-http.service';
import { AdjudicatorPenaltiesService } from './penalties.service';

@Component({
    selector: 'penalties',
    styleUrls: [ './penalties.component.css' ],
    templateUrl: './penalties.component.html'
})
export class PenaltiesComponent implements OnInit {
    public penaltiesForEntry: Penalty[];

    @Input()
    public model: Penalty;
    public entry: Entry;

    private penalties: Penalty[];
    private entries: Entry[];
    private races: Race[];
    private events: Event[] = new Array<Event>();
    private clubs: Club[] = new Array<Club>();
    private entryId: number;

    constructor(
        private route: ActivatedRoute,
        private racesService: AdjudicatorRacesService,
        private entriesService: EntriesService,
        private clubsService: ClubsService,
        private eventsService: EventsService,
        private penaltiesService: AdjudicatorPenaltiesService,
        private location: Location

    ) {}

    public ngOnInit() {
        this.model = new Penalty();
        this.model.time = 0;
        this.entriesService.getEntries().subscribe((entries: Entry[]) => {
            this.entries = entries;
            if (this.entryId) {
                this.entry = this.getEntryForEntryId(this.entryId);
                this.penaltiesForEntry = this.getPenaltiesForEntry(this.entry);
            }
        });

        this.penaltiesService.getPenalties().subscribe((penalties: Penalty[]) => {
            this.penalties = penalties;
            if (this.entry) {
                this.penaltiesForEntry = this.getPenaltiesForEntry(this.entry);
            }
        });

        this.route.params.subscribe((params: Params) => {
            this.entryId = +params['entryId'];
            this.entry = this.getEntryForEntryId(this.entryId);
            if (this.penalties && this.entry) {
                this.penaltiesForEntry = this.getPenaltiesForEntry(this.entry);
            }
        });

        this.clubsService.getClubs().subscribe((clubs: Club[]) => {
            this.clubs = clubs;
        });

        this.racesService.getRaces().subscribe((races: Race[]) => {
            this.races = races;
        });

        this.eventsService.getEvents().subscribe((events: Event[]) => {
            this.events = events;
        });
    }

    public getClubNamesForEntry(entry: Entry): string {
        let clubs: string;
        for (let clubId of entry.clubs) {
            let club: Club = this.clubsService.getClubForId(clubId);
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

    public enterTimeToModel(time: number) {
        this.model.time = time;
    }

    public addPenaltyForEntry() {
        this.model.entry = this.entry.id;
        this.penaltiesService.createPenalty(this.model);
        this.model = new Penalty();
        this.model.time = 0;
    }

    public removePenalty(penalty: Penalty): void {
        this.penaltiesService.removePenalty(penalty);
    }

    public updatePenalty(penalty: Penalty): void {
        this.penaltiesService.updatePenalty(penalty);
    }

    public getRaceNameForEntry(entry: Entry): string {
        let race: Race = this.racesService.getRaceForId(entry.race);
        if (race) {
            return race.name;
        }
        return null;
    }

    public getEventNameForEntry(entry: Entry): string {
        let event: Event = this.eventsService.getEventForId(entry.event);
        if (event) {
            return event.name;
        }
        return null;
    }

    public enterTime(timeStamp: number, penalty: Penalty) {
        penalty.time = timeStamp;
    }

    public goBack(): void {
        this.location.back();
    }

    private getEntryForEntryId(entryId: number): Entry {
        let entry: Entry;
        if (this.entries) {
            entry = this.entries.filter((e: Entry) => e.id === entryId).shift();
        }
        return entry;
    }

    private getPenaltiesForEntry(entry: Entry): Penalty[] {
        return this.penaltiesService.getPenaltiesForEntry(entry);
    }
}
