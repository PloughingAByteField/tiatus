import { Component, Input, Output, EventEmitter } from '@angular/core';

import { ConverstationMessage } from '../../messages/converstation-message.model';
import { Connected } from '../../messages/connected.model';

@Component({
  selector: 'messaging',
  styleUrls: [ './messaging.component.css' ],
  templateUrl: './messaging.component.html',
})
export class MessagingComponent {
  @Input()
  public messages: ConverstationMessage[];
  @Input()
  public connected: Connected[];

  @Output()
  public newMessage: EventEmitter<ConverstationMessage> = new EventEmitter<ConverstationMessage>();

  public onClick(): void {
    let newMessage: ConverstationMessage = new ConverstationMessage();
    newMessage.from = 'me';
    newMessage.to = 'me';
    newMessage.message = 'hello';
    this.newMessage.next(newMessage);
  }
}
