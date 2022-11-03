import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { ISettingsCargaApp } from '../settings-carga-app.model';
import { sampleWithRequiredData, sampleWithNewData, sampleWithPartialData, sampleWithFullData } from '../settings-carga-app.test-samples';

import { SettingsCargaAppService } from './settings-carga-app.service';

const requireRestSample: ISettingsCargaApp = {
  ...sampleWithRequiredData,
};

describe('SettingsCargaApp Service', () => {
  let service: SettingsCargaAppService;
  let httpMock: HttpTestingController;
  let expectedResult: ISettingsCargaApp | ISettingsCargaApp[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(SettingsCargaAppService);
    httpMock = TestBed.inject(HttpTestingController);
  });

  describe('Service methods', () => {
    it('should find an element', () => {
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.find(123).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should create a SettingsCargaApp', () => {
      // eslint-disable-next-line @typescript-eslint/no-unused-vars
      const settingsCargaApp = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(settingsCargaApp).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a SettingsCargaApp', () => {
      const settingsCargaApp = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(settingsCargaApp).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a SettingsCargaApp', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of SettingsCargaApp', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a SettingsCargaApp', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addSettingsCargaAppToCollectionIfMissing', () => {
      it('should add a SettingsCargaApp to an empty array', () => {
        const settingsCargaApp: ISettingsCargaApp = sampleWithRequiredData;
        expectedResult = service.addSettingsCargaAppToCollectionIfMissing([], settingsCargaApp);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(settingsCargaApp);
      });

      it('should not add a SettingsCargaApp to an array that contains it', () => {
        const settingsCargaApp: ISettingsCargaApp = sampleWithRequiredData;
        const settingsCargaAppCollection: ISettingsCargaApp[] = [
          {
            ...settingsCargaApp,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addSettingsCargaAppToCollectionIfMissing(settingsCargaAppCollection, settingsCargaApp);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a SettingsCargaApp to an array that doesn't contain it", () => {
        const settingsCargaApp: ISettingsCargaApp = sampleWithRequiredData;
        const settingsCargaAppCollection: ISettingsCargaApp[] = [sampleWithPartialData];
        expectedResult = service.addSettingsCargaAppToCollectionIfMissing(settingsCargaAppCollection, settingsCargaApp);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(settingsCargaApp);
      });

      it('should add only unique SettingsCargaApp to an array', () => {
        const settingsCargaAppArray: ISettingsCargaApp[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const settingsCargaAppCollection: ISettingsCargaApp[] = [sampleWithRequiredData];
        expectedResult = service.addSettingsCargaAppToCollectionIfMissing(settingsCargaAppCollection, ...settingsCargaAppArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const settingsCargaApp: ISettingsCargaApp = sampleWithRequiredData;
        const settingsCargaApp2: ISettingsCargaApp = sampleWithPartialData;
        expectedResult = service.addSettingsCargaAppToCollectionIfMissing([], settingsCargaApp, settingsCargaApp2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(settingsCargaApp);
        expect(expectedResult).toContain(settingsCargaApp2);
      });

      it('should accept null and undefined values', () => {
        const settingsCargaApp: ISettingsCargaApp = sampleWithRequiredData;
        expectedResult = service.addSettingsCargaAppToCollectionIfMissing([], null, settingsCargaApp, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(settingsCargaApp);
      });

      it('should return initial array if no SettingsCargaApp is added', () => {
        const settingsCargaAppCollection: ISettingsCargaApp[] = [sampleWithRequiredData];
        expectedResult = service.addSettingsCargaAppToCollectionIfMissing(settingsCargaAppCollection, undefined, null);
        expect(expectedResult).toEqual(settingsCargaAppCollection);
      });
    });

    describe('compareSettingsCargaApp', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareSettingsCargaApp(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.compareSettingsCargaApp(entity1, entity2);
        const compareResult2 = service.compareSettingsCargaApp(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.compareSettingsCargaApp(entity1, entity2);
        const compareResult2 = service.compareSettingsCargaApp(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.compareSettingsCargaApp(entity1, entity2);
        const compareResult2 = service.compareSettingsCargaApp(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
