export enum MessageType {
    ADD = 1,
    DELETE,
    UPDATE
}

export function convertStringToMessageType(obj: string): MessageType {
    return MessageType[obj];
}
