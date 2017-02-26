import { Component } from '@angular/core';

import { ConfigService } from '../../services/config.service';

@Component({
  selector: 'titlebar',
  styleUrls: [ './titlebar.component.css' ],
  templateUrl: './titlebar.component.html',
})
export class TitlebarComponent {
  private logo: string;
  private eventTitle: string;

  constructor(private configService: ConfigService) {}

  public getLogo(): string {
    if ( !this.logo) {
      this.logo = this.configService.getLogo();
    }

    return this.logo;
  }

  public getEventTitle(): string {
    if ( !this.eventTitle) {
      this.eventTitle = this.configService.getEventTitle();
    }

    return this.eventTitle;
  }
}
