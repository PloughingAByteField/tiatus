import { Component, OnInit } from '@angular/core';

import { ConfigService } from '../../config/config.service';

@Component({
  selector: 'titlebar',
  styleUrls: [ './titlebar.component.css' ],
  templateUrl: './titlebar.component.html',
})
export class TitlebarComponent implements OnInit {
  private logo: string;
  private eventTitle: string;

  constructor(private configService: ConfigService) {}

  public ngOnInit() {
    if ( !this.logo) {
      this.configService.getLogo().subscribe((data: string) => {
        this.logo = data;
      });
    }
    if ( !this.eventTitle) {
      this.configService.getEventTitle().subscribe((data: string) => {
        this.eventTitle = data;
      });
    }
  }

  public getLogo(): string {
    return this.logo;
  }

  public getEventTitle(): string {
    return this.eventTitle;
  }
}
