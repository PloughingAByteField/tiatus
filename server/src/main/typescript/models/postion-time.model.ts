export class PositionTime {
    public synced: boolean;
    public startPoint: boolean;
    public time: number;
    public position: number;
    public entry: number;
}

export function convertJsonToPositionTime(json: PositionTime): PositionTime {
    let positionTime: PositionTime = new PositionTime();
    positionTime.synced = json.synced;
    positionTime.startPoint = json.startPoint;
    positionTime.time = json.time;
    positionTime.position = json.position;
    positionTime.entry = json.entry;
    return positionTime;
}
