import os
from platform import node

DEVELOPMENT_HOST = ['pcamsod.cern.ch', 'ams-backup-test.cern.ch','ams-backup-dev.cern.ch', 'training-apashnin.cern.ch']
PRODUCTION_HOST  = ['ams-backup.cern.ch','pcbck00.cern.ch']

RAW_INCOMPLETE_DIR = '/afs/cern.ch/ams/Offline/RawFileInfo/Incomplete'
RAW_SUMMARY_DIR    = '/afs/cern.ch/ams/Offline/RawFileInfo/Summary'

if node() in DEVELOPMENT_HOST:
    APP_NAME        = 'pocc_backup_dev'
    LOG_DIR         = '/tmp'
    LOCK_DIR        = '/tmp'
    ARCHIVE_DIR     = '/tmp/archive'
    FRAMES_DIR      = '/tmp/FRAMES'
    FRAMES_DIR_OLD  = '/tmp/FRAMES'
    FRAMES_DIR_MOUNT= '/tmp/FRAMES_MOUNT'
    CASTOR_HOST     = 'root://castorpublic.cern.ch'
    CASTOR_ROOT_DIR = '/castor/cern.ch/user/o/odemakov/Data/FRAMES'
    #EOS_HOST        = 'root://eosams.cern.ch'
    #EOS_ROOT_DIR    = '/eos/ams/group/ams-hardware/frames_backup/test'
    SOC_ROOT_DIR    = '/tmp/SOC'
    CHD_LOG_DIR     = '/tmp/CHD'
    CHD_FILES_DIR   = '/tmp/CHD/HKHR/RT/'
    DMOG_DIR        = '/tmp/DataMissingOnGround'
    PLAYBACK_DIR    = '/tmp/Playback'
elif node() in PRODUCTION_HOST:
    APP_NAME        = 'pocc_backup'
    LOG_DIR         = '/dat0/frames_backup/log'
    LOCK_DIR        = '/dat0/frames_backup/lock'
    ARCHIVE_DIR     = '/dat0/frames_backup/archive'
    FRAMES_DIR      = '/tmp_mnt/pcposc0_Data/FRAMES'
    FRAMES_DIR_OLD  = '/dat0/FRAMES'
    FRAMES_DIR_MOUNT= '/mnt/ams_backup_dat0'
    CASTOR_HOST     = 'root://castorpublic.cern.ch'
    CASTOR_ROOT_DIR = '/castor/cern.ch/user/a/ams/Data/FRAMES'
    #EOS_HOST        = 'root://eosams.cern.ch'
    #EOS_ROOT_DIR    = '/eos/ams/group/ams-hardware/frames_backup'
    SOC_ROOT_DIR    = '/tmp_mnt/pcamsj0_Data/data'
    CHD_LOG_DIR     = '/dat0/frames_backup/CHD'
    CHD_FILES_DIR   = '/tmp_mnt/pcposc0_Data/CHD/HKHR/RT/'
    DMOG_DIR        = '/tmp_mnt/pcposp0_pocchome/data/DataMissingOnGround'
    PLAYBACK_DIR    = '/tmp_mnt/pcposp0_pocchome/data/Playback'
else:
    raise Exception("Host %s not allowed to execute this command" % os.uname()[1])
