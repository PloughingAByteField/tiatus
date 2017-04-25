import { ConverstationMessage } from '../../messages/converstation-message.model';

export class MessagePosition {
    public position: string;
    public messages: ConverstationMessage[] = new Array<ConverstationMessage>();
}
