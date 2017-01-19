import { Component } from '@angular/core';
import { ActivatedRoute, Params } from '@angular/router';
import { Observable } from 'rxjs/Observable';

import { Http } from "@angular/http";

import { Entry, EntriesService } from '../../services/entries.service';
import { Club, ClubsService } from '../../services/clubs.service';

@Component({
    selector: 'timing',
    styleUrls: [ './timing.component.css' ],
    templateUrl: './timing.component.html'
})
export class TimingComponent {
    private _raceId: number = 0;
    private entries: Entry[];
    private clubs: Club[];
    public raceEntries: Entry[];

    page: number = 1;

    constructor(private entriesService: EntriesService, private clubsService: ClubsService, private route: ActivatedRoute, private http: Http) {
        this.entriesService.getEntries()
        .subscribe((data: Entry[]) => {
            this.entries = data;
            this.filterRace(this._raceId);
        });
        this.clubsService.getClubs()
        .subscribe((data: Club[]) => {
            this.clubs = data;
        });
    }

    private filterRace(raceId: number) {
        if (this.entries) {
            this.entries.filter((entry: Entry) => entry.race.id === raceId);
        }
    }

    getClubNames(clubs): string {
        return clubs.map((club) => club.clubName).join(" / ");
    }

    onKey(event:any, entry: any) {
        console.log(event);
        console.log(entry);
    }

    ngOnInit() {
        console.log('hello from timing');

        this.route.params.subscribe((params: Params) => {
            this._raceId = +params['raceId'];
            this.filterRace(this._raceId);
        });
    }
}