import { Component, OnInit, Input } from '@angular/core';
import { ActivatedRoute, Params } from '@angular/router';
import { Location }                 from '@angular/common';

import { Observable } from 'rxjs/Observable';

import { TranslateService } from 'ng2-translate';
import { Penalty } from '../../models/penalty.model';
import { Entry } from '../../entries/entry.model';
import { convertToTimeStamp, convertFromTimeStamp } from '../../models/postion-time.model';

import { EntriesService } from '../../entries/entries.service';
import { AdjudicatorHttpPenaltiesService } from '../http-services/penalties.service';
import { AdjudicatorPenaltiesService } from '../services/penalties.service';

@Component({
    selector: 'penalties',
    styleUrls: [ './penalties.component.css' ],
    templateUrl: './penalties.component.html'
})
export class PenaltiesComponent implements OnInit {
    public penaltiesForEntry: Penalty[];

    @Input()
    public model: Penalty = new Penalty();
    public entry: Entry;

    private penalties: Penalty[];
    private entries: Entry[];
    private entryId: number;

    constructor(
        private route: ActivatedRoute,
        private translate: TranslateService,
        private entriesService: EntriesService,
        private penaltiesService: AdjudicatorPenaltiesService,
        private location: Location

    ) {}

    public ngOnInit() {
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
    }

    public addPenaltyForEntry() {
        this.model.entry = this.entry.id;
        this.penaltiesService.createPenalty(this.model);
        this.model = new Penalty();
    }

    public removePenalty(penalty: Penalty): void {
        this.penaltiesService.removePenalty(penalty);
    }

    public updatePenalty(penalty: Penalty): void {
        this.penaltiesService.updatePenalty(penalty);
    }

    public getPenaltyTime(penalty: Penalty): string {
        return convertFromTimeStamp(penalty.time);
    }

    public enterTime(value: string, penalty: Penalty) {
        if (value) {
            let timeStamp: number = convertToTimeStamp(value);
            penalty.time = timeStamp;
        }
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
