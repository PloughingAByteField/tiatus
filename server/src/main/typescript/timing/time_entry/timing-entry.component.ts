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
    public raceEntries: Entry[];
    public page: number = 1;
    public itemsPerPage: number = 2;
    public filteredClubs: Club[];

    private raceId: number = 0;
    private entries: Entry[];
    private clubs: Club[];

    constructor(
        private entriesService: EntriesService,
        private clubsService: ClubsService,
        private route: ActivatedRoute
    ) {
        this.entriesService.getEntries()
        .subscribe((data: Entry[]) => {
            this.entries = data;
            this.filterRace(this.raceId);
        });
        this.clubsService.getClubs()
        .subscribe((data: Club[]) => {
            this.clubs = data;
            this.filteredClubs = this.clubs;
            // this.filteredClubs = this.filterClubs('2');
        });
    }

    public getClubNames(clubs): string {
        return clubs.map((club) => club.clubName).join(' / ');
    }

    public onKey(event: any, entry: any) {
        console.log(event);
        console.log(entry);
    }

    public ngOnInit() {
        console.log('hello from timing');
        this.route.params.subscribe((params: Params) => {
            this.raceId = +params['raceId'];
            this.filterRace(this.raceId);
        });
    }

    public filterClubs(clubsArr: Club[], clubName: string): Club[] {
        console.log('clubName ' + clubName);
        return clubsArr.filter((club: Club) => club.clubName.includes(clubName));
    }

    public clicked(filter1, filter2) {
        this.filteredClubs = this.clubs;
        if (filter1) {
            console.log('filter1');
            this.filteredClubs = this.filterClubs(this.filteredClubs, filter1);
            console.log(this.filteredClubs);
        }
        if (filter2) {
            console.log('filter2');
            this.filteredClubs = this.filterClubs(this.filteredClubs, filter2);
            console.log(this.filteredClubs);
        }
        
    }

    private filterRace(raceId: number) {
        if (this.entries) {
            this.entries.filter((entry: Entry) => entry.race.id === raceId);
        }
    }
}
