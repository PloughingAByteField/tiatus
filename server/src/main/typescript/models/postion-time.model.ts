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

export function convertFromTimeStamp(timeStamp: number): string {
    if ( !timeStamp) {
        return null;
    }

    let date = new Date(timeStamp);
    let hours = date.getUTCHours();
    // Minutes part from the timestamp
    let minutes = '0' + date.getUTCMinutes();
    // Seconds part from the timestamp
    let seconds = '0' + date.getUTCSeconds();
    let milliSeconds = '000' + date.getUTCMilliseconds();
    let time = hours + ':' + minutes.substr(-2) + ':' + seconds.substr(-2);
    time = time + '.' + milliSeconds.substr(-3);
    return time;
};

export function convertToTimeStamp(timeString: string): number {
    if ( !timeString) {
        return null;
    }

    let date = new Date(0);
    date.setUTCMilliseconds(0);
    let millisecondFields = timeString.split('.');
    if (millisecondFields.length === 2) {
        date.setUTCMilliseconds(+millisecondFields[1]);
    }
    let timeFields = timeString.split(':');
    date.setUTCHours(+timeFields[0]);
    date.setUTCMinutes(+timeFields[1]);
    date.setUTCSeconds(+timeFields[2]);
    return date.getTime();
};
