import { Race, convertJsonToRace } from '../../races/race.model';
import { RacePositionTemplateEntry, convertJsonToRacePositionTemplateEntry }
    from './race-position-template-entry.model';

export class RacePositionTemplate {
    public name: string;
    public defaultTemplate: boolean;
    public race: number;
    public templates: RacePositionTemplateEntry[];
    public id: number;
}

export function
    convertJsonToRacePositionTemplate(json: RacePositionTemplate): RacePositionTemplate {
    let template: RacePositionTemplate = new RacePositionTemplate();
    template.name = json.name;
    template.defaultTemplate = json.defaultTemplate;
    template.race = json.race;
    template.id = json.id;
    template.templates = new Array<RacePositionTemplateEntry>();
    json.templates.map((templateJson: RacePositionTemplateEntry) => {
      template.templates.push(convertJsonToRacePositionTemplateEntry(templateJson));
    });
    return template;
}
