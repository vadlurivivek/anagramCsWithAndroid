package com.google.engedu.anagrams;

import android.util.Log;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Random;
import java.util.Arrays;

public class AnagramDictionary {
    private static final int MIN_NUM_ANAGRAMS = 5;
    private static final int DEFAULT_WORD_LENGTH = 3;
    private static final int MAX_WORD_LENGTH = 7;
    private int wordLength = DEFAULT_WORD_LENGTH;
    private Random random = new Random();
    ArrayList<String> words;
    HashMap<Integer,ArrayList<String>> sizeToWords;
    HashMap<String,ArrayList<String>> lettersToWord;
    HashSet<String> wordSet;
    public AnagramDictionary(InputStream wordListStream) throws IOException {
        BufferedReader in = new BufferedReader(new InputStreamReader(wordListStream));
        String line;
        words = new ArrayList<>();
        lettersToWord = new HashMap<>();
        sizeToWords = new HashMap<>();
        wordSet = new HashSet<>();
        while((line = in.readLine()) != null) {
            String word = line.trim();
            words.add(word);
            String sortedWord = sortLetters(word);
            if(!sizeToWords.containsKey(word.length())){
                ArrayList<String> sizeToWordsList = new ArrayList<>();
                sizeToWordsList.add(word);
                sizeToWords.put(word.length(),sizeToWordsList);
            }else{
                sizeToWords.get(word.length()).add(word);
            }
            if(!(wordSet.contains(sortedWord))){
                wordSet.add(sortedWord);
                ArrayList<String> newList = new ArrayList<>();

                newList.add(word);
                lettersToWord.put(sortedWord, newList);
            }else{
                lettersToWord.get(sortedWord).add(word);
            }
        }
        //Log.d("sdfs","" + lettersToWord.get("opst").size());
    }

    public boolean isGoodWord(String word, String base) {
        return !word.toLowerCase().trim().contains(base.toLowerCase().trim());
        //return true;
    }

    public ArrayList<String> getAnagrams(String targetWord) {
        ArrayList<String> result = new ArrayList<>();
        Iterator<String> itr = words.iterator();

        String sortedTarget = sortLetters(targetWord);
        while(itr.hasNext()){
            String word = itr.next();
            String sortedWord = sortLetters(word);
            if(sortedWord.equals(sortedTarget)){
                    result.add(word);
            }
        }
        return result;
    }

    public ArrayList<String> getAnagramsWithOneMoreLetter(String word) {
        ArrayList<String> result = new ArrayList<>();
        char[] alphabet = "abcdefghijklmnopqrstuvwxyz".toCharArray();
        for(int i=0;i<26;i++){
            String newWord = word + alphabet[i];
            String sortedWord = sortLetters(newWord);
            if(!lettersToWord.containsKey(sortedWord))
                continue;
            ArrayList<String> newList = lettersToWord.get(sortedWord);
            Iterator<String> itr = newList.iterator();

            while(itr.hasNext()){
                String isCorrectWord = itr.next();
                if(isGoodWord(isCorrectWord, word)) {
                    result.add(isCorrectWord);
                }
            }
        }
        return result;
    }

    public String pickGoodStarterWord() {
        String result = "stop";
        if(wordLength < MAX_WORD_LENGTH) {
            ArrayList<String> sizeToWordsList = sizeToWords.get(wordLength);
            int rdm = random.nextInt(sizeToWordsList.size());
            for (int i = rdm; i < sizeToWordsList.size(); i++) {
                String str = sizeToWordsList.get(i);
                if (sizeToWordsList.size() > MIN_NUM_ANAGRAMS) {
                    result = str;
                    break;
                }
            }
        }
        wordLength++;
        return result;
    }

    public String sortLetters(String word){
        char[] toBeSortedWord = word.toCharArray();
        Arrays.sort(toBeSortedWord);
        String sortedWord = new String(toBeSortedWord);
        return sortedWord;
    }
}
