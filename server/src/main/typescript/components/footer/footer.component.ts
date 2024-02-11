import { Component, OnInit } from '@angular/core';

import { ConfigService } from '../../config/config.service';

@Component({
  selector: 'footer',
  styleUrls: [ './footer.component.css' ],
  templateUrl: './footer.component.html'
})
export class FooterComponent implements OnInit {

  public footer: string;


  constructor(private configService: ConfigService) {}

  public ngOnInit() {
    this.configService.getFooter().subscribe((data: string) => {
      if (data) {
        this.footer = ' / ' + data;
      }
    });
  }

  public getFooter(): string {
    return this.footer;
  }
}
