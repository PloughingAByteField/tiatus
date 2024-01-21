import { Injectable, OnDestroy } from '@angular/core';

import { BehaviorSubject } from 'rxjs';
import { Subscription } from 'rxjs';

import { RacePositionsHttpService } from './race-positions-http.service';
import { RacePositionTemplate, convertObjectToRacePositionTemplate } from './race-position-template.model';

import { Race } from '../../races/race.model';
import { Message } from '../../websocket/message.model';
import { MessageType } from '../../websocket/message-type.model';

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
            const index = this.templates.indexOf(r);
            const sliced = this.templates.splice(index, 1);
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

    public processTemplateMessage(message: Message): void {
        console.log('process message');
        const template: RacePositionTemplate = convertObjectToRacePositionTemplate(message.data);
        console.log(template);
        if (message.type === MessageType.ADD) {
            this.templates.push(template);
        } else if (message.type === MessageType.DELETE) {
            const deletedTemplate: RacePositionTemplate = this.getTemplateForId(template.id);
            if (deletedTemplate !== null) {
                const index = this.templates.indexOf(deletedTemplate);
                const sliced = this.templates.splice(index, 1);
            }
        } else if (message.type === MessageType.UPDATE) {
            const updatedTemplate: RacePositionTemplate = this.getTemplateForId(template.id);
            if (updatedTemplate !== null) {
                updatedTemplate.name = template.name;
                updatedTemplate.race = template.race;
                updatedTemplate.templates = template.templates;
                updatedTemplate.defaultTemplate = template.defaultTemplate;
            }
        }

        this.subject.next(this.templates);
    }

    public getTemplateForId(templateId: number): RacePositionTemplate {
        for (const template of this.templates) {
            if (template.id === templateId) {
                return template;
            }
        }
        return null;
    }
}
