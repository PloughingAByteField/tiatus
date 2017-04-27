import { Injectable } from '@angular/core';

import { BehaviorSubject } from 'rxjs/BehaviorSubject';

import { RacePositionTemplatesHttpService } from './race-position-templates-http.service';
import { RacePositionTemplateEntry, convertObjectToRacePositionTemplateEntry } from './race-position-template-entry.model';

import { Message } from '../../websocket/message.model';
import { MessageType } from '../../websocket/message-type.model';

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
            const index = this.templates.indexOf(r);
            this.templates.splice(index, 1);
            // reoreder
            const updatedTemplates = this.templates
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

    public processTemplateMessage(message: Message): void {
        console.log('process message');
        const template: RacePositionTemplateEntry =
            convertObjectToRacePositionTemplateEntry(message.data);
        console.log(template);
        if (message.type === MessageType.ADD) {
            this.templates.push(template);
        } else if (message.type === MessageType.DELETE) {
            const deletedTemplate: RacePositionTemplateEntry = this.getTemplateForId(template.id);
            if (deletedTemplate !== null) {
                const index = this.templates.indexOf(deletedTemplate);
                const sliced = this.templates.splice(index, 1);
            }
        } else if (message.type === MessageType.UPDATE) {
            const updatedTemplate: RacePositionTemplateEntry = this.getTemplateForId(template.id);
            if (updatedTemplate !== null) {
                updatedTemplate.position = template.position;
                updatedTemplate.positionOrder = template.positionOrder;
                updatedTemplate.template = template.template;
            }
        }

        this.subject.next(this.templates);
    }

    public getTemplateForId(templateId: number): RacePositionTemplateEntry {
        for (const template of this.templates) {
            if (template.id === templateId) {
                return template;
            }
        }
        return null;
    }
}
