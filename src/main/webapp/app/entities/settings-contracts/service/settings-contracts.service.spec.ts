import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { ISettingsContracts } from '../settings-contracts.model';
import { sampleWithRequiredData, sampleWithNewData, sampleWithPartialData, sampleWithFullData } from '../settings-contracts.test-samples';

import { SettingsContractsService } from './settings-contracts.service';

const requireRestSample: ISettingsContracts = {
  ...sampleWithRequiredData,
};

describe('SettingsContracts Service', () => {
  let service: SettingsContractsService;
  let httpMock: HttpTestingController;
  let expectedResult: ISettingsContracts | ISettingsContracts[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(SettingsContractsService);
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

    it('should create a SettingsContracts', () => {
      // eslint-disable-next-line @typescript-eslint/no-unused-vars
      const settingsContracts = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(settingsContracts).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a SettingsContracts', () => {
      const settingsContracts = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(settingsContracts).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a SettingsContracts', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of SettingsContracts', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a SettingsContracts', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addSettingsContractsToCollectionIfMissing', () => {
      it('should add a SettingsContracts to an empty array', () => {
        const settingsContracts: ISettingsContracts = sampleWithRequiredData;
        expectedResult = service.addSettingsContractsToCollectionIfMissing([], settingsContracts);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(settingsContracts);
      });

      it('should not add a SettingsContracts to an array that contains it', () => {
        const settingsContracts: ISettingsContracts = sampleWithRequiredData;
        const settingsContractsCollection: ISettingsContracts[] = [
          {
            ...settingsContracts,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addSettingsContractsToCollectionIfMissing(settingsContractsCollection, settingsContracts);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a SettingsContracts to an array that doesn't contain it", () => {
        const settingsContracts: ISettingsContracts = sampleWithRequiredData;
        const settingsContractsCollection: ISettingsContracts[] = [sampleWithPartialData];
        expectedResult = service.addSettingsContractsToCollectionIfMissing(settingsContractsCollection, settingsContracts);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(settingsContracts);
      });

      it('should add only unique SettingsContracts to an array', () => {
        const settingsContractsArray: ISettingsContracts[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const settingsContractsCollection: ISettingsContracts[] = [sampleWithRequiredData];
        expectedResult = service.addSettingsContractsToCollectionIfMissing(settingsContractsCollection, ...settingsContractsArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const settingsContracts: ISettingsContracts = sampleWithRequiredData;
        const settingsContracts2: ISettingsContracts = sampleWithPartialData;
        expectedResult = service.addSettingsContractsToCollectionIfMissing([], settingsContracts, settingsContracts2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(settingsContracts);
        expect(expectedResult).toContain(settingsContracts2);
      });

      it('should accept null and undefined values', () => {
        const settingsContracts: ISettingsContracts = sampleWithRequiredData;
        expectedResult = service.addSettingsContractsToCollectionIfMissing([], null, settingsContracts, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(settingsContracts);
      });

      it('should return initial array if no SettingsContracts is added', () => {
        const settingsContractsCollection: ISettingsContracts[] = [sampleWithRequiredData];
        expectedResult = service.addSettingsContractsToCollectionIfMissing(settingsContractsCollection, undefined, null);
        expect(expectedResult).toEqual(settingsContractsCollection);
      });
    });

    describe('compareSettingsContracts', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareSettingsContracts(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.compareSettingsContracts(entity1, entity2);
        const compareResult2 = service.compareSettingsContracts(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.compareSettingsContracts(entity1, entity2);
        const compareResult2 = service.compareSettingsContracts(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.compareSettingsContracts(entity1, entity2);
        const compareResult2 = service.compareSettingsContracts(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
