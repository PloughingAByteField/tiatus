import { MessageType, convertStringToMessageType } from './message-type.model';

export class Message {
    public type: MessageType;
    public data: string;
    public objectType: string;
}

export function convertObjectToMessage(obj: any): Message {
    let mesage: Message = new Message();
    mesage.type = convertStringToMessageType(obj.type);
    mesage.data = obj.data;
    mesage.objectType = obj.objectType;
    return mesage;
}
