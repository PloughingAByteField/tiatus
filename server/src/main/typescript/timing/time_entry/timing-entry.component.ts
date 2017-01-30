import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Params } from '@angular/router';
import { Observable } from 'rxjs/Observable';

import { EntriesService } from '../../services/entries.service';
import { ClubsService } from '../../services/clubs.service';
import { Club } from '../../models/club.model';
import { Entry } from '../../models/entry.model';

@Component({
    selector: 'timing-entry',
    styleUrls: [ './timing-entry.component.css' ],
    templateUrl: './timing-entry.component.html'
})
export class TimingEntryComponent implements OnInit {
    private raceId: number = 0;
    private entries: Entry[];
    private clubs: Club[];
    public raceEntries: Entry[];

    page: number = 1;

    constructor(private entriesService: EntriesService, private clubsService: ClubsService, private route: ActivatedRoute) {
        this.entriesService.getEntries()
        .subscribe((data: Entry[]) => {
            this.entries = data;
            this.filterRace(this.raceId);
        });
        this.clubsService.getClubs()
        .subscribe((data: Club[]) => {
            this.clubs = data;
        });
    }

    getClubNames(clubs): string {
        return clubs.map((club) => club.clubName).join(' / ');
    }

    onKey(event:any, entry: any) {
        console.log(event);
        console.log(entry);
    }

    ngOnInit() {
        console.log('hello from timing');
        
        this.route.params.subscribe((params: Params) => {
            this.raceId = +params['raceId'];
            this.filterRace(this.raceId);
        });
    }

    private filterRace(raceId: number) {
        if (this.entries) {
            this.entries.filter((entry: Entry) => entry.race.id === raceId);
        }
    }
}