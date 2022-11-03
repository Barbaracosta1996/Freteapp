import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { ISettingsCargaApp } from '../settings-carga-app.model';

@Component({
  selector: 'jhi-settings-carga-app-detail',
  templateUrl: './settings-carga-app-detail.component.html',
})
export class SettingsCargaAppDetailComponent implements OnInit {
  settingsCargaApp: ISettingsCargaApp | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ settingsCargaApp }) => {
      this.settingsCargaApp = settingsCargaApp;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
