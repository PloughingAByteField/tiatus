import { Component, OnInit, OnDestroy } from '@angular/core';

import { Subscription } from 'rxjs';

import { AdminConfigService } from './config.service';

@Component({
  selector: 'config',
  styleUrls: [ './config.component.css' ],
  templateUrl: './config.component.html'
})
export class ConfigComponent implements OnInit, OnDestroy {

  public logo: string;
  public footer: string;
  public title: string;
  public newLogo: File = null;
  public newFooter: string;
  public newTitle: string;

  private footerSubscription: Subscription;
  private logoSubscription: Subscription;
  private titleSubscription: Subscription;

  constructor(private configService: AdminConfigService) {}

  public ngOnInit() {
    if ( !this.logo) {
      this.logoSubscription = this.configService.getLogo().subscribe((data: string) => {
        this.logo = data;
      });
    }
    if ( !this.title) {
      this.titleSubscription = this.configService.getEventTitle().subscribe((data: string) => {
        this.title = data;
      });
    }
    if ( !this.footer) {
      this.footerSubscription = this.configService.getFooter().subscribe((data: string) => {
        this.footer = data;
      });
    }
  }

  public ngOnDestroy() {
    if (this.footerSubscription) {
      this.footerSubscription.unsubscribe();
    }
    if (this.titleSubscription) {
      this.titleSubscription.unsubscribe();
    }
    if (this.logoSubscription) {
      this.logoSubscription.unsubscribe();
    }
  }

  public onFileChange(event: any): void {
    console.log(event);
    console.log(event.srcElement.files);
    if (event && event.srcElement && event.srcElement.files && event.srcElement.files.length > 0) {
      this.newLogo = event.srcElement.files[0];
      console.log(this.newLogo);
    } else {
      this.newLogo = null;
    }
  }

  public uploadLogo(): void {
    this.configService.uploadLogo(this.newLogo).then((fileName: string) => {
      this.newLogo = null;
    });
  }

  public uploadFooter(): void {
    this.configService.uploadFooter(this.newFooter).then((footer: string) => {
      this.newFooter = null;
    });
  }

  public uploadTitle(): void {
    this.configService.uploadTitle(this.newTitle).then((title: string) => {
      this.newTitle = null;
    });
  }
}
