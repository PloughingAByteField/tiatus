export class RacePositionTemplateEntry {
    public positionOrder: number;
    public position: number;
    public template: number;
    public id: number;
}

export function
    convertJsonToRacePositionTemplateEntry(json: RacePositionTemplateEntry):
        RacePositionTemplateEntry {
    let template: RacePositionTemplateEntry = new RacePositionTemplateEntry();
    template.positionOrder = json.positionOrder;
    template.position = json.position;
    template.template = json.template;
    template.id = json.id;
    return template;
}
