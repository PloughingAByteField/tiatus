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
    let newMessage: ConverstationMessage = new ConverstationMessage();
    newMessage.to = to;
    newMessage.message = data;
    this.newMessage.next(newMessage);
  }

  public respond(): void {
    let newMessage: ConverstationMessage = new ConverstationMessage();
    newMessage.to = 'me';
    newMessage.message = 'hello';
    this.newMessage.next(newMessage);
  }

  public getMessages(mp: MessagePosition): string {
    if (mp && mp.messages.length > 0) {
      return mp.messages[0].message;
    }
    return null;
  }

  private populateMessagesForPositions(message: ConverstationMessage): void {
    console.log(message);
    if (message !== undefined) {
      let mp: MessagePosition = new MessagePosition();
      mp.messages.push(message);
      this.messages.push(mp);
    }
  }
}
