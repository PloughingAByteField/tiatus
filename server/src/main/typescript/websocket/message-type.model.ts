export enum MessageType {
    ADD = 1,
    DELETE,
    UPDATE,
    CONNECTED,
    DISCONNECTED,
    CHAT
}

export function convertStringToMessageType(obj: string): MessageType {
    return MessageType[obj];
}
