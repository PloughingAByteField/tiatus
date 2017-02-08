import { Component, Input, OnInit } from '@angular/core';
import { ActivatedRoute, Params } from '@angular/router';
import { Observable } from 'rxjs/Observable';

import { EntriesService } from '../../services/entries.service';
import { ClubsService } from '../../services/clubs.service';
import { Club } from '../../models/club.model';
import { Entry } from '../../models/entry.model';
import { Position } from '../../models/position.model';

import { TimingPositionService } from '../timing-position.service';

@Component({
    selector: 'timing-entry',
    styleUrls: [ './timing-entry.component.css' ],
    templateUrl: './timing-entry.component.html'
})
export class TimingEntryComponent implements OnInit {
    public raceEntries: Entry[];
    public page: number = 1;
    public itemsPerPage: number = 10;
    public filteredEntries: Entry[];
    public numberFilter: string = '';
    public clubFilter: string = '';
    public eventFilter: string = '';
    public reverseTimeSort = false;
    public reverseSyncedSort = false;

    private position: Position;

    private entries: Entry[];
    private raceId: number = 0;

    constructor(
        private entriesService: EntriesService,
        private clubsService: ClubsService,
        private route: ActivatedRoute,
        private timingPositionService: TimingPositionService
    ) {}

    public ngOnInit() {
        this.entriesService.getEntries()
        .subscribe((data: Entry[]) => {
            this.entries = data;
            this.filteredEntries = this.filterRace(this.raceId);
        });

        this.route.params.subscribe((params: Params) => {
            this.raceId = +params['raceId'];
            this.filteredEntries = this.filterRace(this.raceId);
            this.numberFilter = '';
            this.clubFilter = '';
            this.eventFilter = '';
        });
        this.position = this.timingPositionService.position;
        console.log(this.position);
    }

    public getClubNames(clubs): string {
        return clubs.map((club) => club.clubName).join(' / ');
    }

    public enterTime(value: string) {
        console.log(value);
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
        this.filteredEntries = this.filterEntries();
    }

    public sortTime(direction: string): void {
        console.log('Got direction ' + direction);
        this.reverseTimeSort = !this.reverseTimeSort;
    }

    public sortSynced(direction: string): void {
        console.log('Got direction ' + direction);
        this.reverseSyncedSort = !this.reverseSyncedSort;
    }

    private filterEntries(): Entry[] {
        let filteredEntries: Entry[] = this.filterRace(this.raceId);
        if (this.numberFilter) {
            filteredEntries = this.filterNumbers(filteredEntries, this.numberFilter);
        }
        if (this.clubFilter) {
            filteredEntries = this.filterClubs(filteredEntries, this.clubFilter);
        }
        if (this.eventFilter) {
            filteredEntries = this.filterEvents(filteredEntries, this.eventFilter);
        }

        return filteredEntries;
    }

    private filterNumbers(entries: Entry[], value: string): Entry[] {
        return entries.filter((entry: Entry) => entry.number.toString().includes(value));
    }

    private filterClubs(entries: Entry[], value: string): Entry[] {
        return entries.filter((entry: Entry)  => {
             if (entry.clubs.find((club: Club) => club.clubName.includes(value))) {
                return entry;
             }
        });
    }

    private filterEvents(entries: Entry[], value: string): Entry[] {
        return entries.filter((entry: Entry)  => entry.event.name.includes(value));
    }

    private filterRace(raceId: number): Entry[] {
        if (this.entries) {
            return this.entries.filter((entry: Entry) => entry.race.id === raceId);
        }
    }
}
