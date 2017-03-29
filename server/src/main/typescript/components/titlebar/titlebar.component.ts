import { Component, OnInit, OnDestroy } from '@angular/core';

import { Subscription } from 'rxjs/Subscription';

import { ConfigService } from '../../config/config.service';

@Component({
  selector: 'titlebar',
  styleUrls: [ './titlebar.component.css' ],
  templateUrl: './titlebar.component.html',
})
export class TitlebarComponent implements OnInit, OnDestroy {
  public logo: string;
  public eventTitle: string;

  private logoSubscription: Subscription;
  private titleSubscription: Subscription;

  constructor(private configService: ConfigService) {}

  public ngOnInit() {
    if ( !this.logo) {
      this.logoSubscription = this.configService.getLogo().subscribe((data: string) => {
        this.logo = data;
      });
    }
    if ( !this.eventTitle) {
      this.titleSubscription = this.configService.getEventTitle().subscribe((data: string) => {
        this.eventTitle = data;
      });
    }
  }

  public ngOnDestroy() {
    if (this.titleSubscription) {
      this.titleSubscription.unsubscribe();
    }
    if (this.logoSubscription) {
      this.logoSubscription.unsubscribe();
    }
  }
}
