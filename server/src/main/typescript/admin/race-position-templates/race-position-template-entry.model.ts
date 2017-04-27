export class RacePositionTemplateEntry {
    public positionOrder: number;
    public position: number;
    public template: number;
    public id: number;
}

export function
    convertObjectToRacePositionTemplateEntry(obj: any):
        RacePositionTemplateEntry {
    const template: RacePositionTemplateEntry = new RacePositionTemplateEntry();
    template.positionOrder = obj.positionOrder;
    template.position = obj.position;
    template.template = obj.template;
    template.id = obj.id;
    return template;
}
