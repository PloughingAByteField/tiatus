import { Component, Input, Output, EventEmitter, OnChanges, SimpleChanges } from '@angular/core';

import { ConverstationMessage } from '../../messages/converstation-message.model';
import { Connected } from '../../messages/connected.model';
import { MessagePosition } from './message-position.model';

@Component({
  selector: 'messaging',
  styleUrls: [ './messaging.component.css' ],
  templateUrl: './messaging.component.html',
})
export class MessagingComponent implements OnChanges {
  @Input()
  public message: ConverstationMessage;

  @Input()
  public connected: Connected[];

  public messages: MessagePosition[] = new Array<MessagePosition>();

  @Output()
  public newMessage: EventEmitter<ConverstationMessage> = new EventEmitter<ConverstationMessage>();

  public ngOnChanges(changes: SimpleChanges): void {
    console.log(changes);
    this.populateMessagesForPositions(changes.message.currentValue);
  }

  public sendMessage(to: string, data: string): void {
    const newMessage: ConverstationMessage = new ConverstationMessage();
    newMessage.to = to;
    newMessage.message = data;
    this.newMessage.next(newMessage);
    newMessage.from = 'me:';
    this.populateMessagesForPositions(newMessage);
  }

  public respond(mp: MessagePosition, data: string): void {
    const newMessage: ConverstationMessage = new ConverstationMessage();
    newMessage.to = mp.position;
    newMessage.message = data;
    this.newMessage.next(newMessage);
    newMessage.from = 'me:';
    this.populateMessagesForPositions(newMessage);
  }

  public getMessages(mp: MessagePosition): string {
    if (mp && mp.messages.length > 0) {
      let data: string = '';
      mp.messages.map((message: ConverstationMessage) => {
        data = data.concat(message.from + ': ' + message.message + '\n');
      });
      return data;
    }
    return null;
  }

  private populateMessagesForPositions(message: ConverstationMessage): void {
    console.log(message);
    if (message !== undefined && message !== null) {
      let mp: MessagePosition;
      if (message.to === 'ALL' || message.from === 'me:') {
        mp = this.getMessagePosition(message.to);
      } else {
        mp = this.getMessagePosition(message.from);
      }
      if (mp === null) {
        mp = new MessagePosition();
        if (message.to === 'ALL' || message.from === 'me:') {
          mp.position = message.to;
        } else {
          mp.position = message.from;
        }
        mp.messages.push(message);
        this.messages.push(mp);
      } else {
        mp.messages.push(message);
      }
    }
  }

  private getMessagePosition(position: string): MessagePosition {
    for (const mp of this.messages) {
      if (mp.position !== null && position !== null && position === mp.position) {
        return mp;
      }
    }
    return null;
  }
}
