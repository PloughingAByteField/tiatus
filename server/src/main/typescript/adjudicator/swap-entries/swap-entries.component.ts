import { Component, OnInit, Input } from '@angular/core';
import { ActivatedRoute, Params } from '@angular/router';
import { Location }                 from '@angular/common';

import { Observable } from 'rxjs/Observable';

import { Race } from '../../races/race.model';
import { Entry } from '../../entries/entry.model';
import { convertToTimeStamp, convertFromTimeStamp } from '../../models/postion-time.model';

import { SwapEntriesHttpService } from './swap-entries-http.service';
import { SwapEntriesService } from './swap-entries.service';
import { RacesService } from '../../races/races.service';
import { EntriesService } from '../../entries/entries.service';
import { PenaltiesService } from '../../penalties/penalties.service';
import { DisqualificationService } from '../../disqualification/disqualification.service';
import { TimesService } from '../../times/times.service';

@Component({
    selector: 'swap-entries',
    styleUrls: [ './swap-entries.component.css' ],
    templateUrl: './swap-entries.component.html',
    providers: [ SwapEntriesService, SwapEntriesHttpService ]
})
export class SwapEntriesComponent implements OnInit {

    public entry: Entry;
    public race: Race;
    public from: Entry;
    public to: Entry;

    private races: Race[];
    private entries: Entry[];
    private raceId: number;

    constructor(
        private route: ActivatedRoute,
        private racesService: RacesService,
        private entriesService: EntriesService,
        private swapService: SwapEntriesService,
        private penaltiesService: PenaltiesService,
        private disqualificationService: DisqualificationService,
        private timesService: TimesService,
        private location: Location

    ) {}

    public ngOnInit() {
        this.entriesService.getEntries().subscribe((entries: Entry[]) => {
            this.entries = entries;
        });

        this.racesService.getRaces().subscribe((races: Race[]) => {
            this.races = races;
            if (this.raceId) {
                this.setRaceForRaceId(this.raceId);
            }
        });

        this.route.params.subscribe((params: Params) => {
            this.raceId = +params['raceId'];
            this.setRaceForRaceId(this.raceId);
        });
    }

    public getEntriesForRace(): Entry[] {
        if (this.entries) {
            return this.entries.filter((entry: Entry) => entry.race.id === this.race.id);
        }
        return [];
    }

    public swapEntries(): void {
        this.swapService.swapEntries(this.from, this.to);
        // force refresh as server has done lots of work behind the sences
        this.penaltiesService.refresh();
        this.disqualificationService.refresh();
        this.entriesService.refreshForRace(this.from.race);
        this.timesService.refreshForRace(this.from.race);
    }

    public isSwapEnabled(): boolean {
        if (this.to && this.from && this.to.id !== this.from.id) {
            return true;
        }
        return false;
    }

    public goBack(): void {
        this.location.back();
    }

    private setRaceForRaceId(raceId: number): void {
        if (this.races) {
            this.race = this.racesService.getRaceForId(this.raceId);
        }
    }
}
