import { Injectable } from '@angular/core';

import { BehaviorSubject } from 'rxjs/BehaviorSubject';

import { RacePositionTemplatesHttpService } from './race-position-templates-http.service';
import { RacePositionTemplateEntry } from './race-position-template-entry.model';

@Injectable()
export class RacePositionTemplatesService {

    private templates: RacePositionTemplateEntry[] = new Array<RacePositionTemplateEntry>();
    private subject: BehaviorSubject<RacePositionTemplateEntry[]>
        = new BehaviorSubject<RacePositionTemplateEntry[]>(this.templates);

    constructor(private service: RacePositionTemplatesHttpService) {}

    public getTemplates(): BehaviorSubject<RacePositionTemplateEntry[]> {
        return this.subject;
    }

    public setTemplates(templates: RacePositionTemplateEntry[]): void {
        this.templates = templates;
        this.subject = new BehaviorSubject<RacePositionTemplateEntry[]>(this.templates);
    }

    public addTemplate(template: RacePositionTemplateEntry): void {
        this.service.createTemplateEntry(template).then((r: RacePositionTemplateEntry) => {
            this.templates.push(r);
            this.subject.next(this.templates);
        });
    }

    public removeTemplate(template: RacePositionTemplateEntry): void {
        this.service.removeTemplateEntry(template).then((r: RacePositionTemplateEntry) => {
            let index = this.templates.indexOf(r);
            this.templates.splice(index, 1);
            // reoreder
            let updatedTemplates = this.templates
                .filter((templateToUpdate: RacePositionTemplateEntry) => {
                if (templateToUpdate.positionOrder > r.positionOrder) {
                    templateToUpdate.positionOrder = templateToUpdate.positionOrder - 1;
                    return templateToUpdate;
                }
            });
            updatedTemplates.forEach((templateToUpdate: RacePositionTemplateEntry) =>
                this.updateTemplate(templateToUpdate));

            this.templates.sort((a: RacePositionTemplateEntry, b: RacePositionTemplateEntry) => {
                if (a.positionOrder < b.positionOrder) {
                    return -1;
                } else if (a.positionOrder === b.positionOrder) {
                    return 0;
                } else if (a.positionOrder > b.positionOrder) {
                    return 1;
                }
            });
            this.subject.next(this.templates);
        });
    }

    public updateTemplate(template: RacePositionTemplateEntry): void {
        this.service.updateTemplateEntry(template).then();
    }

}
