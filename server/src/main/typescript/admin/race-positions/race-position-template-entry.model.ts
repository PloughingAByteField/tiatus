export class RacePositionTemplateEntry {
    public positionOrder: boolean;
    public canStart: boolean;
    public finishingPosition: number;
    public startingPosition: number;
    public position: number;
    public template: number;
}

export function
    convertJsonToRacePositionTemplateEntry(json: RacePositionTemplateEntry):
        RacePositionTemplateEntry {
    let template: RacePositionTemplateEntry = new RacePositionTemplateEntry();
    template.positionOrder = json.positionOrder;
    template.canStart = json.canStart;
    template.finishingPosition = json.finishingPosition;
    template.startingPosition = json.startingPosition;
    template.position = json.position;
    template.template = json.template;
    return template;
}
