import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../settings-contracts.test-samples';

import { SettingsContractsFormService } from './settings-contracts-form.service';

describe('SettingsContracts Form Service', () => {
  let service: SettingsContractsFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(SettingsContractsFormService);
  });

  describe('Service methods', () => {
    describe('createSettingsContractsFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createSettingsContractsFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            terms: expect.any(Object),
            contractDefault: expect.any(Object),
            privacy: expect.any(Object),
          })
        );
      });

      it('passing ISettingsContracts should create a new form with FormGroup', () => {
        const formGroup = service.createSettingsContractsFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            terms: expect.any(Object),
            contractDefault: expect.any(Object),
            privacy: expect.any(Object),
          })
        );
      });
    });

    describe('getSettingsContracts', () => {
      it('should return NewSettingsContracts for default SettingsContracts initial value', () => {
        // eslint-disable-next-line @typescript-eslint/no-unused-vars
        const formGroup = service.createSettingsContractsFormGroup(sampleWithNewData);

        const settingsContracts = service.getSettingsContracts(formGroup) as any;

        expect(settingsContracts).toMatchObject(sampleWithNewData);
      });

      it('should return NewSettingsContracts for empty SettingsContracts initial value', () => {
        const formGroup = service.createSettingsContractsFormGroup();

        const settingsContracts = service.getSettingsContracts(formGroup) as any;

        expect(settingsContracts).toMatchObject({});
      });

      it('should return ISettingsContracts', () => {
        const formGroup = service.createSettingsContractsFormGroup(sampleWithRequiredData);

        const settingsContracts = service.getSettingsContracts(formGroup) as any;

        expect(settingsContracts).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing ISettingsContracts should not enable id FormControl', () => {
        const formGroup = service.createSettingsContractsFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewSettingsContracts should disable id FormControl', () => {
        const formGroup = service.createSettingsContractsFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
