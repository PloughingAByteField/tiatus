import { Component } from '@angular/core';
import { ActivatedRoute, Params } from '@angular/router';
import { Observable } from 'rxjs/Observable';

import { LocalDataSource } from 'ng2-smart-table';

import { Entry, EntriesService } from '../../services/entries.service';

@Component({
    selector: 'timing',
    styleUrls: [ './timing.component.css' ],
    templateUrl: './timing.component.html'
})
export class TimingComponent {
    source: LocalDataSource;
    private _raceId: number = 0;
    private entries: Entry[];

    constructor(private entriesService: EntriesService, private route: ActivatedRoute) {
        this.source = new LocalDataSource();

        this.entriesService.getEntries()
        .subscribe((data: Entry[]) => {
            this.entries = data;
            this.filterRace(this._raceId);
        });
    }

    private filterRace(raceId: number) {
        if (this.entries) {
            this.source.load(this.entries.filter((entry: Entry) => entry.race.id === raceId));
        }
    }

    settings = {
      columns: {
        number: {
          title: 'Number'
        },
        crew: {
          title: 'Crew'
        },
        clubs: {
          title: 'Club',
          valuePrepareFunction: (value) => {
            return value.map((club) => club.clubName).join(" / ");
          }
        },
        raceName: {
          title: 'Race'
        }
      }
    };

    ngOnInit() {
        console.log('hello from timing');

        this.route.params.subscribe((params: Params) => {
            this._raceId = +params['raceId'];
            this.filterRace(this._raceId);
        });
    }
}