export class ConverstationMessage {
    public message: string;
    public from: string;
    public to: string;
}

export function convertObjectToConverstationMessage(obj: any):
    ConverstationMessage {
    let message: ConverstationMessage = new ConverstationMessage();
    message.to = obj.to;
    message.from = obj.from;
    message.message = obj.message;
    return message;
}
