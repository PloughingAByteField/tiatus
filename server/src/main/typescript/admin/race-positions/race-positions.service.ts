import { Injectable, OnDestroy } from '@angular/core';

import { BehaviorSubject } from 'rxjs/BehaviorSubject';
import { Subscription } from 'rxjs/Subscription';

import { RacePositionsHttpService } from './race-positions-http.service';
import { RacePositionTemplate } from './race-position-template.model';

import { Race } from '../../races/race.model';

@Injectable()
export class RacePositionsService implements OnDestroy {

    public templates: RacePositionTemplate[] = new Array<RacePositionTemplate>();
    private subject: BehaviorSubject<RacePositionTemplate[]>
        = new BehaviorSubject<RacePositionTemplate[]>(this.templates);
    private subscription: Subscription;
    private requested: boolean = false;

    constructor(private service: RacePositionsHttpService) {}

    public ngOnDestroy() {
        if (this.subscription) {
            this.subscription.unsubscribe();
        }
    }

    public getTemplates(): BehaviorSubject<RacePositionTemplate[]> {
        if (!this.requested) {
            this.requested = true;
            this.refresh();
        }
        return this.subject;
    }

    public addTemplate(template: RacePositionTemplate): void {
        this.service.createTemplate(template).then((r: RacePositionTemplate) => {
            this.templates.push(r);
            this.subject.next(this.templates);
        });
    }

    public removeTemplate(template: RacePositionTemplate): void {
        this.service.removeTemplate(template).then((r: RacePositionTemplate) => {
            let index = this.templates.indexOf(r);
            let sliced = this.templates.splice(index, 1);
            this.subject.next(this.templates);
        });
    }

    public updateTemplate(template: RacePositionTemplate): void {
        this.service.updateTemplate(template).then();
    }

    public getTemplatesForRace(race: Race): RacePositionTemplate[] {
        return this.templates.filter((template: RacePositionTemplate) => template.race === race.id);
    }

    public refresh(): void {
        this.subscription =
            this.service.getTemplates().subscribe((templates: RacePositionTemplate[]) => {
            this.templates = templates;
            this.subject.next(this.templates);
        });
    }
}
