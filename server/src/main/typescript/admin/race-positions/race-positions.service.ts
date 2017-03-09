import { Injectable } from '@angular/core';

import { BehaviorSubject } from 'rxjs/BehaviorSubject';

import { RacePositionsHttpService } from './race-positions-http.service';
import { RacePositionTemplate } from './race-position-template.model';

@Injectable()
export class RacePositionsService {

    public templates: RacePositionTemplate[] = new Array<RacePositionTemplate>();
    private subject: BehaviorSubject<RacePositionTemplate[]>
        = new BehaviorSubject<RacePositionTemplate[]>(this.templates);

    constructor(private service: RacePositionsHttpService) {
        this.refresh();
    }

    public getTemplates(): BehaviorSubject<RacePositionTemplate[]> {
        return this.subject;
    }

    public addTemplate(template: RacePositionTemplate): void {
        console.log('Add');
        console.log(template);
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

    public refresh(): void {
        this.service.getTemplates().subscribe((templates: RacePositionTemplate[]) => {
            this.templates = templates;
            this.subject.next(this.templates);
        });
    }
}
