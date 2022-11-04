import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { ISettingsContracts } from '../settings-contracts.model';
import { DataUtils } from 'app/core/util/data-util.service';

@Component({
  selector: 'jhi-settings-contracts-detail',
  templateUrl: './settings-contracts-detail.component.html',
})
export class SettingsContractsDetailComponent implements OnInit {
  settingsContracts: ISettingsContracts | null = null;

  constructor(protected dataUtils: DataUtils, protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ settingsContracts }) => {
      this.settingsContracts = settingsContracts;
    });
  }

  byteSize(base64String: string): string {
    return this.dataUtils.byteSize(base64String);
  }

  openFile(base64String: string, contentType: string | null | undefined): void {
    this.dataUtils.openFile(base64String, contentType);
  }

  previousState(): void {
    window.history.back();
  }
}
