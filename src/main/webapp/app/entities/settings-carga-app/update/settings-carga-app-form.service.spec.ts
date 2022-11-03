import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../settings-carga-app.test-samples';

import { SettingsCargaAppFormService } from './settings-carga-app-form.service';

describe('SettingsCargaApp Form Service', () => {
  let service: SettingsCargaAppFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(SettingsCargaAppFormService);
  });

  describe('Service methods', () => {
    describe('createSettingsCargaAppFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createSettingsCargaAppFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            rdCode: expect.any(Object),
            waToken: expect.any(Object),
          })
        );
      });

      it('passing ISettingsCargaApp should create a new form with FormGroup', () => {
        const formGroup = service.createSettingsCargaAppFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            rdCode: expect.any(Object),
            waToken: expect.any(Object),
          })
        );
      });
    });

    describe('getSettingsCargaApp', () => {
      it('should return NewSettingsCargaApp for default SettingsCargaApp initial value', () => {
        // eslint-disable-next-line @typescript-eslint/no-unused-vars
        const formGroup = service.createSettingsCargaAppFormGroup(sampleWithNewData);

        const settingsCargaApp = service.getSettingsCargaApp(formGroup) as any;

        expect(settingsCargaApp).toMatchObject(sampleWithNewData);
      });

      it('should return NewSettingsCargaApp for empty SettingsCargaApp initial value', () => {
        const formGroup = service.createSettingsCargaAppFormGroup();

        const settingsCargaApp = service.getSettingsCargaApp(formGroup) as any;

        expect(settingsCargaApp).toMatchObject({});
      });

      it('should return ISettingsCargaApp', () => {
        const formGroup = service.createSettingsCargaAppFormGroup(sampleWithRequiredData);

        const settingsCargaApp = service.getSettingsCargaApp(formGroup) as any;

        expect(settingsCargaApp).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing ISettingsCargaApp should not enable id FormControl', () => {
        const formGroup = service.createSettingsCargaAppFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewSettingsCargaApp should disable id FormControl', () => {
        const formGroup = service.createSettingsCargaAppFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
