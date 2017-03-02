import { Component, OnInit, Input } from '@angular/core';
import { ActivatedRoute, Params } from '@angular/router';
import { Location }                 from '@angular/common';

import { Observable } from 'rxjs/Observable';

import { TranslateService } from 'ng2-translate';
import { Disqualification } from '../../models/disqualification.model';
import { Entry } from '../../models/entry.model';
import { convertToTimeStamp, convertFromTimeStamp } from '../../models/postion-time.model';

import { EntriesService } from '../../http-services/entries.service';
import { AdjudicatorHttpDisqualificationsService }
    from '../http-services/disqualifications.service';
import { AdjudicatorDisqualificationService } from '../services/disqualification.service';

@Component({
    selector: 'disqualification',
    styleUrls: [ './disqualification.component.css' ],
    templateUrl: './disqualification.component.html'
})
export class DisqualificationComponent implements OnInit {

    @Input()
    public model: Disqualification = new Disqualification();
    public entry: Entry;

    private disqualifications: Disqualification[];
    private entries: Entry[];
    private entryId: number;

    constructor(
        private route: ActivatedRoute,
        private translate: TranslateService,
        private entriesService: EntriesService,
        private disqualificationService: AdjudicatorDisqualificationService,
        private location: Location

    ) {}

    public ngOnInit() {
        this.entriesService.getEntries().subscribe((entries: Entry[]) => {
            this.entries = entries;
            if (this.entryId) {
                this.entry = this.getEntryForEntryId(this.entryId);
                this.model = this.getDisqualificationForEntry(this.entry);
            }
        });

        this.disqualificationService.getDisqualifications()
            .subscribe((disqualifications: Disqualification[]) => {
                this.disqualifications = disqualifications;
                if (this.entry) {
                    this.model = this.getDisqualificationForEntry(this.entry);
                }
        });

        this.route.params.subscribe((params: Params) => {
            this.entryId = +params['entryId'];
            this.entry = this.getEntryForEntryId(this.entryId);
            if (this.disqualifications && this.entry) {
                this.model = this.getDisqualificationForEntry(this.entry);
            }
        });
    }

    public isDisqualified(): boolean {
        if (this.model.id) {
            return true;
        }
        return false;
    }

    public addDisqualificationForEntry() {
        this.model.entry = this.entry.id;
        this.disqualificationService.disqualify(this.model);
        this.model = new Disqualification();
    }

    public removeDisqualificationForEntry(): void {
        this.disqualificationService.removeDisqualification(this.model);
        this.model = new Disqualification();
    }

    public updateDisqualificationForEntry(): void {
        this.disqualificationService.updateDisqualification(this.model);
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

    private getDisqualificationForEntry(entry: Entry): Disqualification {
        if (this.disqualifications) {
            let disqualification: Disqualification = this.disqualifications
                .filter((d: Disqualification) => d.entry === entry.id).shift();
            if (disqualification) {
                return disqualification;
            }
        }

        return this.model;
    }
}
