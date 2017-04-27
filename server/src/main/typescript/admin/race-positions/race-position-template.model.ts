import { Race, convertObjectToRace } from '../../races/race.model';
import { RacePositionTemplateEntry, convertObjectToRacePositionTemplateEntry } from '../race-position-templates/race-position-template-entry.model';

export class RacePositionTemplate {
    public name: string;
    public defaultTemplate: boolean;
    public race: number;
    public templates: RacePositionTemplateEntry[];
    public id: number;
}

export function
    convertObjectToRacePositionTemplate(obj: any): RacePositionTemplate {
        const template: RacePositionTemplate = new RacePositionTemplate();
        template.name = obj.name;
        template.defaultTemplate = obj.defaultTemplate;
        template.race = obj.race;
        template.id = obj.id;
        template.templates = new Array<RacePositionTemplateEntry>();
        obj.templates.map((templateObj: RacePositionTemplateEntry) => {
        template.templates.push(convertObjectToRacePositionTemplateEntry(templateObj));
        });
        return template;
}
